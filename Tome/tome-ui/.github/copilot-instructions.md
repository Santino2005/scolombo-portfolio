# Copilot Instructions (Frontend)

Contexto

- Stack: TypeScript, Next.js 15 (App Router), Tailwind CSS v4, shadcn/ui, Lucide, Auth0, ESLint, Prettier, Docker.
- Package manager: pnpm (obligatorio).
- MCP conectados: Figma, Linear, GitHub, Playwright.

Reglas generales

- Usar Server Components por defecto; marcar “use client” solo si hay interacción (formularios, efectos, eventos).
- No hardcodear colores, tamaños ni rutas; usar tokens del tema y utilidades Tailwind.
- Mantener accesibilidad; no remover aria/role de shadcn/ui; preferir roles y labels estándar en tests.
- Tipar todo: props, retornos y respuestas de API; evitar any sin justificación.
- No usar npm o yarn en sugerencias; solo pnpm.

Estructura del proyecto

- app/: páginas y layouts (App Router); api/: route handlers (BFF).
- components/: ui/ (wrappers/variantes de shadcn), common/ (compartidos), <dominio>/ (específicos).
- lib/: types/, utils/, api/ (fetch server-side).
- Respetar nombres: PascalCase para componentes, useCamelCase para hooks, page.tsx/layout.tsx/loading.tsx/error.tsx para rutas.

Tema y estilos (globals.css)

- Los tokens del Design System se exponen como:
  - Variables de DS: --ds-\* (en :root).
  - Variables semánticas shadcn: --background, --primary, --secondary, etc.
  - Tailwind v4 @theme inline: --color-_, --spacing-_, radios y tipografías.
  - Utilidades tipográficas en @layer utilities con prefijo u-\* (ej.: u-text-title-medium).
- Siempre usar clases basadas en tokens: bg-background, text-foreground, border-border, ring-ring, text-muted-foreground, bg-muted, etc.
- Para portadas: usar grid-covers en el contenedor (mantiene ancho de portada).
- Dark mode existe bajo .dark, no activar por defecto; si se usa, verificar contrastes.

shadcn/ui

- Extender mediante wrappers en components/ui; no modificar la librería.
- Mantener props de accesibilidad.
- Instalar componentes con pnpm:
  - pnpm dlx shadcn@latest add <componente>
  - Ej.: pnpm dlx shadcn@latest add badge

Data fetching y BFF

- Fetch en Server Components usando API_BASE_URL.
- Mutaciones y lógica sensible vía route handlers en app/api (BFF), no desde el cliente.
- Paginación en listas grandes; usar loading.tsx (skeletons), error.tsx y estados vacíos.

Auth0

- Manejar sesiones en Server Components o BFF; no exponer secretos ni usar localStorage para tokens.
- Usar helpers/SDK compatibles con App Router.

Calidad, PRs y CI

- ESLint + Prettier: formatear y lint antes de cada commit/merge.
- PRs pequeños y enfocados; no mezclar refactor grande con features nuevas.
- CI debe pasar format, lint, build y tests.
- Revisar: tipado, tokens, nombres, imports explícitos, accesibilidad.

Comandos (usar pnpm siempre)

- Instalar dependencias: pnpm install
- Agregar dependencia: pnpm add <paquete>
- Agregar dependencia de dev: pnpm add -D <paquete>
- Ejecutar scripts: pnpm <script>
- shadcn: pnpm dlx shadcn@latest add <componente>

Patrones de generación

- Componentes: colocar en components/ui o components/<dominio>; Tailwind con tokens; sin hex/rgba hardcodeados; tipado estricto.
- Páginas: Server Component por defecto; loading.tsx y error.tsx cuando aplique; fetch server-side o vía BFF.
- Formularios: Client Components; validar en cliente y servidor cuando los datos sean sensibles.
- Tipografías: usar utilidades u-text-\* para overrides consistentes.
- Grids de portadas: usar grid-covers + gap con tokens de spacing.

Checklist por cambio

- Server vs Client correctamente elegido.
- Usa tokens de tema y utilidades Tailwind (nada hardcodeado).
- Estructura y nombres correctos.
- Tipos definidos (sin any innecesario).
- Estados: loading, error, empty cuando corresponda.
- Accesibilidad asegurada (roles/aria, focus).
- E2E planificados/agregados para flujos críticos.
- Comandos y dependencias con pnpm.
- CI pasa format, lint, build y tests.

Integraciones MCP

- Figma: referenciar tokens por nombre; no insertar valores literales de Figma; mapear solo semánticas (--ds-\* y variables shadcn).
- Linear: incluir referencia al issue en ramas/PRs; redactar descripciones con alcance y criterios de aceptación.
- GitHub: plantillas de PR con checklist de tokens, accesibilidad y pruebas; commits atómicos, claros.
