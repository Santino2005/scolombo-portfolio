"use client";

import React from "react";
import { BookOpen, GraduationCap, MapPin, Award, User, Calendar } from "lucide-react";

export default function About() {
  const profileDetails = [
    { icon: <User className="text-primary" size={20} />, label: "Age", value: "21 years old" },
    { icon: <GraduationCap className="text-primary" size={20} />, label: "University", value: "Universidad Austral, Pilar (4th Year)" },
    { icon: <Award className="text-primary" size={20} />, label: "Academic GPA", value: "7 / 10 (~2.8 / 4.0)" },
    { icon: <MapPin className="text-primary" size={20} />, label: "Location", value: "Pilar, Buenos Aires, Argentina" },
    { icon: <BookOpen className="text-primary" size={20} />, label: "High School", value: "Instituto Verbo Divino" },
    { icon: <Calendar className="text-primary" size={20} />, label: "Languages", value: "Spanish (Native), English (B1-B2)" },
  ];

  return (
    <section id="about-me" className="py-24 bg-white border-y border-border/40 scroll-mt-10">
      <div className="max-w-5xl mx-auto px-6">
        {/* Section Title */}
        <div className="text-center mb-16">
          <h2 className="font-sans text-xs font-semibold text-primary uppercase tracking-widest mb-3">Profile</h2>
          <p className="font-sans text-3xl sm:text-4xl font-extrabold text-foreground">About Me</p>
          <div className="w-12 h-1 bg-primary mx-auto mt-4 rounded-full" />
        </div>

        <div className="grid grid-cols-1 md:grid-cols-12 gap-10 items-start">
          {/* Detailed Narrative */}
          <div className="md:col-span-7 space-y-6">
            <h3 className="font-sans text-xl font-bold text-foreground">
              Rigorous Training &amp; Software Engineering
            </h3>
            <p className="font-sans text-muted leading-relaxed">
              I am a junior <strong className="font-semibold text-foreground">Computer Engineering</strong> student at <strong className="font-semibold text-foreground">Universidad Austral</strong> in Pilar. 
              Throughout my academic background and personal projects, I have focused on designing and building 
              robust, scalable software solutions, always prioritizing code quality, clean design, and thorough testing.
            </p>
            <p className="font-sans text-muted leading-relaxed">
              I have a strong inclination toward <strong className="font-semibold text-foreground">backend architecture</strong> and <strong className="font-semibold text-foreground">systems design</strong>. I am passionate about 
              studying and applying design patterns, and I am currently deepening my knowledge of Test-Driven Development 
              (<strong className="font-semibold text-foreground">TDD</strong>) by analyzing <strong className="font-semibold text-foreground">Kent Beck's</strong> original paper.
            </p>
            <p className="font-sans text-muted leading-relaxed">
              In frontend development, I aim to create fluid, responsive, and highly polished user interfaces, 
              following modern UX/UI principles inspired by the minimalist aesthetics of Vercel, Linear, and Stripe. 
              I also have foundational experience in native mobile development on <strong className="font-semibold text-foreground">Android</strong> using <strong className="font-semibold text-foreground">Kotlin</strong> and <strong className="font-semibold text-foreground">Jetpack Compose</strong>.
            </p>
          </div>

          {/* Quick Profile Cards */}
          <div className="md:col-span-5 grid grid-cols-1 gap-4">
            <h3 className="font-sans text-lg font-bold text-foreground mb-2">
              Key Details
            </h3>
            <div className="grid grid-cols-1 gap-3.5">
              {profileDetails.map((detail, index) => (
                <div
                  key={index}
                  className="flex items-center gap-4 p-4 bg-background border border-border/50 rounded-xl hover:border-primary/20 transition-all duration-300 shadow-sm"
                >
                  <div className="p-2.5 bg-primary/5 rounded-lg">
                    {detail.icon}
                  </div>
                  <div>
                    <p className="text-xs font-semibold text-muted tracking-wider uppercase">
                      {detail.label}
                    </p>
                    <p className="text-sm font-bold text-foreground mt-0.5">
                      {detail.value}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Print-only section mapping CV presentation */}
        <div className="hidden print-only print-section mt-8">
          <h2 className="text-lg font-bold uppercase tracking-wider text-primary border-b pb-1 mb-3">Education</h2>
          <div className="space-y-4">
            <div>
              <div className="flex justify-between font-bold">
                <span>Computer Engineering (4th Year)</span>
                <span>2023 - Present</span>
              </div>
              <div className="flex justify-between text-muted text-sm">
                <span>Universidad Austral, Pilar</span>
                <span>GPA: 7/10</span>
              </div>
            </div>
            <div>
              <div className="flex justify-between font-bold">
                <span>Secondary Education</span>
                <span>Graduated 2022</span>
              </div>
              <div className="flex justify-between text-muted text-sm">
                <span>Instituto Verbo Divino</span>
                <span>Catholic Formation</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
