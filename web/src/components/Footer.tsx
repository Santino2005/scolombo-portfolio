"use client";

import React from "react";

export default function Footer() {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="py-8 bg-background border-t border-border/30 text-center no-print">
      <div className="max-w-5xl mx-auto px-6 flex flex-col sm:flex-row items-center justify-between gap-4">
        <p className="font-sans text-xs text-muted">
          &copy; {currentYear} Colombo Santino. All rights reserved.
        </p>
        <p className="font-sans text-xs text-muted">
          Designed with premium aesthetics &amp; developed in Next.js
        </p>
      </div>
    </footer>
  );
}
