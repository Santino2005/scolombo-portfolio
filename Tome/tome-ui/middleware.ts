import type { NextRequest } from 'next/server';
import { NextResponse } from 'next/server';
import { auth0 } from './lib/auth0';

export async function middleware(request: NextRequest) {
  // Excluir archivos estáticos por extensión
  const staticExtensions = [
    '.png',
    '.jpg',
    '.jpeg',
    '.svg',
    '.webp',
    '.gif',
    '.ico',
    '.css',
    '.js',
  ];
  if (staticExtensions.some((ext) => request.nextUrl.pathname.endsWith(ext))) {
    return NextResponse.next();
  }

  const authRes = await auth0.middleware(request);

  // authentication routes — let the middleware handle it
  if (request.nextUrl.pathname.startsWith('/auth')) {
    return authRes;
  }

  // public routes — no need to check for session
  if (request.nextUrl.pathname === '/') {
    return authRes;
  }

  const { origin } = new URL(request.url);
  const session = await auth0.getSession(request);

  // user does not have a session — redirect to log in
  if (!session) {
    return NextResponse.redirect(`${origin}/auth/login`);
  }

  // access token is expired - redirect to log in
  try {
    await auth0.getAccessToken();
  } catch {
    return NextResponse.redirect(`${origin}/auth/login`);
  }

  return authRes;
}

export const config = {
  matcher: [
    /*
     * Match all request paths except for the ones starting with:
     * - _next/static (static files)
     * - _next/image (image optimization files)
     * - favicon.ico, sitemap.xml, robots.txt (metadata files)
     * - archivos estáticos comunes (png, jpg, jpeg, svg, webp, gif, ico, css, js)
     */
    '/((?!_next/static|_next/image|favicon.ico|sitemap.xml|robots.txt).*)',
  ],
};
