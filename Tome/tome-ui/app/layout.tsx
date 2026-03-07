import type { Metadata } from 'next';
import './globals.css';
import { SidebarProvider, SidebarInset } from '@/components/ui/sidebar';
import { AppSidebar } from '@/components/common/app-sidebar';
import { Toaster } from 'sonner';
import React from 'react';
import { auth0 } from '@/lib/auth0';

export const metadata: Metadata = {
  title: 'TOME',
  description: 'Application for managing your book collection and reading progress',
};

export default async function RootLayout({ children }: { children: React.ReactNode }) {
  const session = await auth0.getSession();

  if (session) {
    return (
      <html lang="en">
        <body className="antialiased">
          <SidebarProvider>
            <AppSidebar />
            <SidebarInset>
              {children}
              <Toaster richColors position="top-right" />
            </SidebarInset>
          </SidebarProvider>
        </body>
      </html>
    );
  } else {
    return (
      <html lang="en" className="scroll-smooth">
        <body className="antialiased">{children}</body>
      </html>
    );
  }
}
