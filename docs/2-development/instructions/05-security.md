# 05 - Security Practices

## Overview

Security is implemented at multiple layers:

- **Gateway:** OAuth2 authentication, rate-limiting
- **API:** Input validation, authorization
- **Database:** Parameterized queries (via Spring Data JPA)
- **Frontend:** XSS prevention (React escaping), CSRF tokens

---

## Input Validation

### Backend Validation (Service Layer)

All input must be validated in the **service layer** before processing:

```java
@Service
public class BrickCreationService {

    public Brick createBrick(BrickCreationForm form) {
        // 1. Validate input
        if (form.getNumber() == null || form.getNumber().trim().isEmpty()) {
            throw new IllegalArgumentException(\"Le numero de brique est obligatoire.\");
        }
        
        if (form.getNumber().length() > 100) {
            throw new IllegalArgumentException(\"Le numero ne peut pas depasser 100 caracteres.\");
        }

        // 2. Validate uniqueness
        brickRepository.findByNumber(form.getNumber()).ifPresent(existing -> {
            throw new IllegalArgumentException(\"Le numero \" + form.getNumber() + \" est deja utilise.\");
        });

        // 3. Sanitize & store
        BrickEntity entity = new BrickEntity();
        entity.setNumber(form.getNumber().trim()); // Trim whitespace
        entity.setTitle(form.getTitle().trim());
        entity.setTags(tagsToCsv(form.getTags()));

        return BrickModelMapper.toModel(brickRepository.save(entity));
    }
}
```

**Rules:**
- Validate **presence** (required fields)
- Validate **format** (email, URL, length)
- Validate **uniqueness** (no duplicates where needed)
- Validate **range** (min/max for numbers)
- Trim and normalize strings
- Never trust user input

### Frontend Validation (Form Layer)

React Hook Form handles validation:

```typescript
const {
  register,
  formState: { errors },
} = useFormContext<BrickFormValues>();

<TextField
  label=\"Brick Number\"
  fullWidth
  error={Boolean(errors.number)}
  helperText={errors.number?.message}
  {...register('number', {
    required: 'Number is required.',
    minLength: {
      value: 1,
      message: 'Number cannot be empty.',
    },
    maxLength: {
      value: 100,
      message: 'Number cannot exceed 100 characters.',
    },
    validate: {
      noSpecialChars: (value) =>
        /^[a-zA-Z0-9-_]+$/.test(value) || 'Only alphanumeric, hyphens, underscores allowed.',
    },
  })}
/>
```

**Rules:**
- Validate on blur (better UX)
- Show inline error messages
- Client-side is UX; server-side is security (don't rely on client validation)

---

## Authentication & Authorization

### Gateway: OAuth2

The gateway enforces OAuth2 authentication:

```yaml
# application-docker.yaml (gateway)
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://auth2.tiogars.fr
          jwk-set-uri: https://auth2.tiogars.fr/.well-known/jwks.json
```

**Protected Routes:** All API requests go through the gateway and require valid JWT token.

### Frontend: OIDC Integration

Frontend uses OIDC client for authentication:

```typescript
// auth/OidcAuthProvider.tsx
export const OidcAuthProvider: FC<OidcAuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    const userManager = new UserManager({
      authority: 'https://auth2.tiogars.fr',
      client_id: 'data-web',
      redirect_uri: window.location.origin,
      response_type: 'code',
      scope: 'openid profile email',
    });

    userManager.getUser().then((user) => {
      setUser(user?.profile ?? null);
    });
  }, []);

  return (
    <OidcAuthContext.Provider value={{ user, /* ... */ }}>
      {children}
    </OidcAuthContext.Provider>
  );
};
```

**Rules:**
- Use OIDC for authentication (not custom JWT)
- Store tokens in browser securely (HttpOnly cookies when possible)
- Validate tokens server-side on every request

---

## Rate Limiting

### Gateway: Bucket4j

Rate-limiting is configured via the gateway (Bucket4j):

```yaml
# application.yaml (gateway)
data:
  gateway:
    rate-limit:
      capacity: 120        # Max requests
      period: PT1M         # Time window (1 minute)
      tokens: 1            # Cost per request
```

When limit exceeded: **429 Too Many Requests**

**Rules:**
- Rate-limit per IP (via `X-Forwarded-For` header)
- Generous limits for internal APIs, stricter for public endpoints
- Document rate limits in API docs

---

## SQL Injection Prevention

### Use Parameterized Queries

Spring Data JPA handles parameterization automatically:

```java
// SAFE: Spring Data uses parameterized queries
Optional<BrickEntity> brick = brickRepository.findByNumber(userInput);

// If custom query needed, use parameterized approach:
@Query(\"SELECT b FROM BrickEntity b WHERE b.number = :number\")
Optional<BrickEntity> findByNumber(@Param(\"number\") String number);

// NEVER do string concatenation:
// NEVER: \"SELECT * FROM brick WHERE number = '\" + userInput + \"'\";
```

**Rules:**
- Always use Spring Data JPA methods (no JDBC)
- Use `@Param` in custom `@Query` annotations
- Never concatenate SQL strings

---

## Cross-Site Scripting (XSS) Prevention

### React Escaping

React automatically escapes strings in JSX:

```typescript
// SAFE: React escapes the value
const username = \"<script>alert('xss')</script>\";
<Typography>{username}</Typography>
// Renders as text, not executed

// UNSAFE: Using dangerouslySetInnerHTML
<Typography dangerouslySetInnerHTML={{ __html: userContent }} />
// NEVER use this unless content is verified safe
```

**Rules:**
- Use normal JSX (escaped by default)
- Avoid `dangerouslySetInnerHTML`
- If HTML is necessary, sanitize with `DOMPurify`

```typescript
import DOMPurify from 'dompurify';

const sanitized = DOMPurify.sanitize(userContent);
<Typography dangerouslySetInnerHTML={{ __html: sanitized }} />
```

---

## CSRF Protection

### Spring Security CSRF Token

CSRF protection is handled by Spring Security (enabled by default):

```java
// Spring automatically validates CSRF tokens
// Token is sent in POST/PUT/DELETE requests
```

### Frontend: Including CSRF Token

RTK Query includes CSRF token automatically (via interceptor if configured):

```typescript
// The gateway/server expects CSRF token in X-CSRF-TOKEN header
// RTK Query can be configured to include it:
// (Already handled by gateway configuration)
```

**Rules:**
- Ensure CSRF tokens are included in POST/PUT/DELETE requests
- Use SameSite cookie policy (default: Lax)

---

## Data Protection

### Encryption at Rest

Database uses PostgreSQL encryption (handled by infrastructure):

```yaml
# Data at rest: Encrypted by PostgreSQL/container
# Data in transit: HTTPS only (enforced by reverse proxy)
```

### Sensitive Data Handling

Never log or store sensitive data unnecessarily:

```java
// BAD: Log password
logger.info(\"User created with password: \" + password);

// GOOD: Log only relevant info
logger.info(\"User created with email: \" + email);

// MASKED: Log sensitive fields partially
logger.info(\"Image data size: \" + imageBase64.length() + \" bytes\");
```

---

## Dependency Security

### Java/Maven

Keep dependencies updated to patch vulnerabilities:

```bash
# Check for known vulnerabilities
mvn org.owasp:dependency-check-maven:check

# Update dependencies
mvn versions:display-dependency-updates
```

### NPM/Frontend

```bash
# Check for vulnerabilities
pnpm audit

# Fix automatically
pnpm audit --fix
```

**Rules:**
- Review and apply security patches promptly
- Keep lock files up-to-date
- Use dependency scanning in CI/CD

---

## Logging & Monitoring

### Structured Logging

Log important security events:

```java
logger.info(\"User {} authenticated successfully\", userId);
logger.warn(\"Authentication failed for user {}\", userId);
logger.error(\"Unauthorized access attempt from IP: {}\", clientIp);

// NEVER log:
// - Passwords
// - Tokens
// - Credit card numbers
// - Personal identification numbers
```

### Observability

Grafana (LGTM stack) monitors:
- Error rates
- Request latency
- Authentication failures
- Rate-limit violations

Access at: http://localhost:3000

---

## Environment-Specific Security

### Development

```yaml
# application.yml (dev)
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8081  # Local test authority
```

### Production

```yaml
# application-prod.yml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://auth2.tiogars.fr  # Real authority
          jwk-set-uri: https://auth2.tiogars.fr/.well-known/jwks.json
```

**Rules:**
- Different credentials for each environment
- Rotate secrets regularly
- Never commit secrets (use `.env` or secrets manager)

---

## Security Checklist

Before deploying:

- [ ] All inputs validated (length, format, uniqueness)
- [ ] Parameterized queries used (no string concatenation in SQL)
- [ ] Sensitive data not logged
- [ ] HTTPS enabled (TLS 1.2+)
- [ ] Authentication enforced on all endpoints
- [ ] Rate-limiting configured
- [ ] Dependencies scanned for vulnerabilities
- [ ] Error messages don't leak implementation details
- [ ] CORS configured appropriately
- [ ] Security headers present (CSP, X-Frame-Options, etc.)

---

## References

- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [Spring Security Docs](https://spring.io/projects/spring-security)
- [React Security Best Practices](https://reactjs.org/docs/dom-elements.html#dangerouslysetinnerhtml)
- [DOMPurify Library](https://github.com/cure53/DOMPurify)
- [Bucket4j Documentation](https://github.com/vladimir-bukhtoyarov/bucket4j)
