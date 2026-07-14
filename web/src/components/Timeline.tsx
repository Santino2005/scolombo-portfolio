"use client";

import React from "react";
import { GraduationCap, Briefcase } from "lucide-react";

export default function Timeline() {
  const timelineItems = [
    {
      year: "2026",
      title: "Bravito Platform Development",
      category: "Project",
      icon: <Briefcase className="text-white" size={16} />,
      description: "Designed and built the Bravito web application, implementing optimized workflows and a secure scalable structure.",
    },
    {
      year: "2023 - Present",
      title: "Computer Engineering (4th Year)",
      category: "Education",
      icon: <GraduationCap className="text-white" size={16} />,
      description: "Pursuing a Bachelor's Degree in Computer Engineering at Universidad Austral, Pilar, Buenos Aires. Focused on systems design, distributed architectures, and software quality.",
    },
  ];

  return (
    <section id="timeline" className="py-24 bg-background/50 grid-bg scroll-mt-10">
      <div className="max-w-4xl mx-auto px-6">
        {/* Section Title */}
        <div className="text-center mb-16">
          <h2 className="font-sans text-xs font-semibold text-primary uppercase tracking-widest mb-3">Trajectory</h2>
          <p className="font-sans text-3xl sm:text-4xl font-extrabold text-foreground">Timeline</p>
          <div className="w-12 h-1 bg-primary mx-auto mt-4 rounded-full" />
        </div>

        {/* Vertical Timeline */}
        <div className="relative border-l border-primary/20 ml-4 md:ml-32 space-y-12 no-print">
          {timelineItems.map((item, index) => (
            <div key={index} className="relative timeline-item pl-8 md:pl-10">
              {/* Dot Icon Indicator */}
              <div className="absolute -left-[17px] top-0 bg-primary border-4 border-background w-8 h-8 rounded-full flex items-center justify-center shadow-sm">
                {item.icon}
              </div>

              {/* Date Column for wide viewports */}
              <div className="hidden md:block absolute -left-36 top-1 text-right w-28">
                <span className="font-mono text-xs font-bold text-primary bg-primary/5 px-2.5 py-1 rounded border border-primary/10">
                  {item.year}
                </span>
              </div>

              {/* Main Content Card */}
              <div className="bg-white border border-border/40 p-6 rounded-2xl shadow-sm hover:shadow-md hover:border-primary/10 transition-all duration-300">
                {/* Date for Mobile */}
                <div className="md:hidden inline-block mb-2.5">
                  <span className="font-mono text-[10px] font-bold text-primary bg-primary/5 px-2 py-0.5 rounded border border-primary/10">
                    {item.year}
                  </span>
                </div>

                <p className="font-mono text-[10px] font-extrabold text-accent uppercase tracking-widest mb-1.5">
                  {item.category}
                </p>
                <h3 className="font-sans text-lg font-bold text-foreground mb-2">
                  {item.title}
                </h3>
                <p className="font-sans text-sm text-muted leading-relaxed">
                  {item.description}
                </p>
              </div>
            </div>
          ))}
        </div>

        {/* Print-only layout */}
        <div className="hidden print-only print-section">
          <h2 className="text-lg font-bold uppercase tracking-wider text-primary border-b pb-1 mb-4">Key Experience &amp; Projects</h2>
          <div className="space-y-4">
            {timelineItems.map((item, index) => (
              <div key={index} className="timeline-item">
                <div className="flex justify-between font-bold text-sm">
                  <span>{item.title}</span>
                  <span className="text-xs text-muted font-normal">{item.year}</span>
                </div>
                <p className="text-xs text-muted mt-0.5">{item.description}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}
