# Build stage
FROM node:26-alpine AS builder

# Install the version declared by package.json without relying on shell detection.
RUN npm install --global pnpm@11.8.0

# Set working directory
WORKDIR /app

# Copy package files and install dependencies
COPY package.json pnpm-lock.yaml pnpm-workspace.yaml ./

# Install dependencies with frozen lockfile to ensure consistency
RUN pnpm install --frozen-lockfile

# Copy the rest of the application code
COPY . .

# Build the application
RUN pnpm run build

# Runtime stage
FROM nginx:1.31-alpine

# Copy built static assets
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy nginx configuration for SPA routing
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose nginx default HTTP port
EXPOSE 80