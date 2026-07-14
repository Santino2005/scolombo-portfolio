"use client";

import React from "react";
import { FolderGit2, Code, GraduationCap, CheckCircle } from "lucide-react";

export default function Stats() {
  const statsList = [
    {
      icon: <FolderGit2 className="text-primary" size={28} />,
      value: "8+",
      label: "Comprehensive Projects",
      description: "Modular monoliths, IoT, and complete e-commerce applications.",
    },
    {
      icon: <Code className="text-accent" size={28} />,
      value: "7",
      label: "Languages Mastered",
      description: "Java, Kotlin, TypeScript, JS, SQL, Python, C++.",
    },
    {
      icon: <GraduationCap className="text-secondary" size={28} />,
      value: "4th Year",
      label: "Computer Engineering",
      description: "Universidad Austral, Pilar.",
    },
    {
      icon: <CheckCircle className="text-primary" size={28} />,
      value: "80%+",
      label: "Test Coverage",
      description: "Strict testing practices using JaCoCo and Vitest.",
    },
  ];

  return (
    <section id="stats" className="py-20 bg-background/50 grid-bg scroll-mt-10 no-print">
      <div className="max-w-5xl mx-auto px-6">
        {/* Section Title */}
        <div className="text-center mb-14">
          <h2 className="font-sans text-xs font-semibold text-primary uppercase tracking-widest mb-3">Metrics</h2>
          <p className="font-sans text-3xl font-extrabold text-foreground">Key Statistics</p>
          <div className="w-12 h-1 bg-primary mx-auto mt-4 rounded-full" />
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {statsList.map((stat, index) => (
            <div
              key={index}
              className="bg-surface border border-border/40 p-6 rounded-2xl shadow-sm hover:shadow-md hover:border-primary/10 transition-all duration-300 flex flex-col items-center text-center group"
            >
              <div className="p-3 bg-background rounded-2xl group-hover:scale-105 transition-transform duration-300">
                {stat.icon}
              </div>
              <p className="font-sans text-4xl font-extrabold text-foreground mt-4 tracking-tight">
                {stat.value}
              </p>
              <p className="font-sans text-sm font-bold text-foreground/80 mt-1">
                {stat.label}
              </p>
              <p className="font-sans text-xs text-muted mt-2 leading-relaxed">
                {stat.description}
              </p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
