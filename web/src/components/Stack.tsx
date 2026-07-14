"use client";

import React from "react";
import { Server, Layout, Database, Wrench, ShieldAlert, Cpu } from "lucide-react";

export default function Stack() {
  const categories = [
    {
      title: "Languages",
      icon: <Cpu className="text-primary" size={20} />,
      items: ["Java (21/17)", "Kotlin (JVM)", "TypeScript", "JavaScript", "Python", "C++ (Arduino)", "SQL", "HTML5 & CSS3"],
    },
    {
      title: "Backend & Communications",
      icon: <Server className="text-accent" size={20} />,
      items: ["Spring Boot (3.x & 4.0)", "Spring Data JPA", "Spring Security", "OAuth2 / JWT", "REST APIs", "WebSockets (STOMP)", "MQTT", "Spring Mail", "Spring WebFlux"],
    },
    {
      title: "Databases & Cache",
      icon: <Database className="text-secondary" size={20} />,
      items: ["PostgreSQL", "MySQL", "Redis (Cache & PubSub)", "H2 (Test/Dev)"],
    },
    {
      title: "Frontend & Mobile",
      icon: <Layout className="text-primary" size={20} />,
      items: ["React (19/18)", "Next.js (App Router)", "Vite", "Tailwind CSS (v4)", "shadcn/ui", "Radix UI", "Jetpack Compose", "Android SDK", "Capacitor"],
    },
    {
      title: "Infrastructure & DevOps",
      icon: <Wrench className="text-accent" size={20} />,
      items: ["AWS", "Azure", "Docker", "Docker Compose", "Supabase Storage", "Railway", "New Relic", "GitHub Actions", "TeamCity CI"],
    },
    {
      title: "Quality, Testing & Linters",
      icon: <ShieldAlert className="text-secondary" size={20} />,
      items: ["JUnit 5", "Mockito", "Testcontainers", "Vitest", "Playwright", "JaCoCo (Coverage)", "Checkstyle", "Spotless", "Spotbugs", "ktlint"],
    },
  ];

  return (
    <section id="skills" className="py-24 bg-white scroll-mt-10">
      <div className="max-w-5xl mx-auto px-6">
        {/* Section Title */}
        <div className="text-center mb-16">
          <h2 className="font-sans text-xs font-semibold text-primary uppercase tracking-widest mb-3">Tools</h2>
          <p className="font-sans text-3xl sm:text-4xl font-extrabold text-foreground">Tech Stack</p>
          <div className="w-12 h-1 bg-primary mx-auto mt-4 rounded-full" />
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 no-print">
          {categories.map((cat, idx) => (
            <div
              key={idx}
              className="bg-background border border-border/40 p-6 rounded-2xl hover:border-primary/10 transition-all duration-300 shadow-sm flex flex-col group"
            >
              <div className="flex items-center gap-3 mb-5 border-b border-border/40 pb-3">
                <div className="p-2 bg-primary/5 rounded-lg group-hover:scale-105 transition-transform">
                  {cat.icon}
                </div>
                <h3 className="font-sans text-base font-bold text-foreground">
                  {cat.title}
                </h3>
              </div>

              <div className="flex flex-wrap gap-2">
                {cat.items.map((item, itemIdx) => (
                  <span
                    key={itemIdx}
                    className="font-sans text-xs font-medium px-2.5 py-1 bg-white border border-border/50 text-foreground/80 hover:text-primary hover:border-primary/20 rounded-full transition-all duration-150 shadow-sm"
                  >
                    {item}
                  </span>
                ))}
              </div>
            </div>
          ))}
        </div>

        {/* Print-only layout showing the exact stack in a clean columns structure */}
        <div className="hidden print-only print-section">
          <h2 className="text-lg font-bold uppercase tracking-wider text-primary border-b pb-1 mb-4">Technical Competencies</h2>
          <div className="print-grid">
            {categories.map((cat, idx) => (
              <div key={idx} className="mb-4">
                <h3 className="font-bold text-sm mb-1">{cat.title}</h3>
                <p className="text-sm text-muted">{cat.items.join(", ")}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}
