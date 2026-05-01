# GitHub Copilot Instructions

## Responsive UI Default Principle

For all web UI features, responsive behavior is required by default.

- Mobile-first layout: content should stack vertically and remain easy to scan and use.
- Desktop/tablet: use tables for dense tabular data.
- Mobile: replace tables with list/card views containing the same essential information.
- Never rely on horizontal scrolling as the primary way to access core data on mobile.
- Keep actions and key metadata available in both desktop and mobile representations.

## Implementation Notes

- Prefer explicit breakpoint-driven rendering (for example: desktop component + mobile component).
- Maintain feature parity between desktop table view and mobile list view.
- Preserve accessibility (readable labels, clear hierarchy, touch-friendly targets).

## API Generation Rules

For data-web API services generated from OpenAPI:

- Always regenerate API services from the OpenAPI spec instead of manually editing generated service files.
- Generated files must be treated as read-only.
- Primary generated target in this repository: data-web/src/services/sectionApi.ts.
- If API contracts change on the server, run generation scripts from data-web:
	- pnpm run openapi:pull
	- pnpm run rtk:codegen:section
	- or pnpm run generate:section-api
- If a change is needed in generated output, update the source of truth first (backend OpenAPI contract and/or codegen config), then regenerate.
