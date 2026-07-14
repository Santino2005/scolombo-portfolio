"use client";

import React from "react";
import { CheckCircle2 } from "lucide-react";

export default function Skills() {
  const mainSkills = [
    { name: "Backend Development (Java/Kotlin, Spring Boot)", level: 95, color: "bg-primary" },
    { name: "Software Quality & Testing (TDD, JaCoCo, Mockito)", level: 92, color: "bg-accent" },
    { name: "Frontend Development (React, Next.js, TypeScript)", level: 88, color: "bg-primary" },
    { name: "Design Patterns & Software Architecture", level: 86, color: "bg-accent" },
    { name: "DevOps & Cloud (Docker, AWS, Azure, CI/CD)", level: 80, color: "bg-primary" },
    { name: "Mobile Development (Android, Jetpack Compose, Capacitor)", level: 75, color: "bg-accent" },
  ];

  const softSkills = [
    "Analytical and systems thinking",
    "Strong focus on software engineering best practices",
    "Team player in complex projects and groups",
    "Continuous capacity for self-learning",
    "Solving complex technical problems",
    "Technical and professional English communication (B1-B2)",
  ];

  return (
    <section id="skills-levels" className="py-24 bg-white border-b border-border/40 scroll-mt-10 no-print">
      <div className="max-w-5xl mx-auto px-6">
        {/* Section Title */}
        <div className="text-center mb-16">
          <h2 className="font-sans text-xs font-semibold text-primary uppercase tracking-widest mb-3">Levels</h2>
          <p className="font-sans text-3xl sm:text-4xl font-extrabold text-foreground">Skills &amp; Competencies</p>
          <div className="w-12 h-1 bg-primary mx-auto mt-4 rounded-full" />
        </div>

        <div className="grid grid-cols-1 md:grid-cols-12 gap-10">
          {/* Main Skills with Progress Bars */}
          <div className="md:col-span-7 space-y-6">
            <h3 className="font-sans text-lg font-bold text-foreground mb-4">
              Core Technical Competencies
            </h3>
            <div className="space-y-5">
              {mainSkills.map((skill, index) => (
                <div key={index} className="space-y-2">
                  <div className="flex justify-between items-center text-sm font-semibold text-foreground/80">
                    <span className="font-sans">{skill.name}</span>
                    <span className="font-mono text-xs text-primary">{skill.level}%</span>
                  </div>
                  <div className="h-2 w-full bg-background rounded-full overflow-hidden border border-border/20">
                    <div
                      className={`h-full rounded-full transition-all duration-1000 ${skill.color}`}
                      style={{ width: `${skill.level}%` }}
                    />
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Soft Skills & Practices */}
          <div className="md:col-span-5 bg-background border border-border/40 p-6 rounded-2xl shadow-sm flex flex-col justify-center">
            <h3 className="font-sans text-lg font-bold text-foreground mb-5">
              Professional Skills
            </h3>
            <div className="space-y-4">
              {softSkills.map((skill, index) => (
                <div key={index} className="flex items-start gap-3">
                  <CheckCircle2 className="text-accent mt-0.5 shrink-0" size={18} />
                  <span className="font-sans text-sm text-muted leading-relaxed">
                    {skill}
                  </span>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
