"use client";

import React from "react";
import { motion } from "framer-motion";
import { Github, Linkedin, Mail, FileText, ArrowRight } from "lucide-react";

export default function Hero() {
  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.15,
      },
    },
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 25 },
    visible: { opacity: 1, y: 0, transition: { duration: 0.6, ease: "easeOut" as const } },
  };

  return (
    <section className="relative min-h-[90vh] flex items-center justify-center pt-24 overflow-hidden grid-bg">
      {/* Decorative gradient blur background */}
      <div className="absolute top-1/4 left-1/4 -translate-x-1/2 -translate-y-1/2 w-72 h-72 sm:w-96 sm:h-96 bg-primary/10 rounded-full blur-3xl -z-10 animate-pulse duration-[8000ms]" />
      <div className="absolute bottom-1/4 right-1/4 translate-x-1/2 translate-y-1/2 w-64 h-64 sm:w-80 sm:h-80 bg-accent/5 rounded-full blur-3xl -z-10 animate-pulse duration-[6000ms]" />

      <div className="max-w-4xl mx-auto px-6 text-center">
        {/* Print-only Header for CV */}
        <div className="hidden print-only text-left mb-6">
          <h1 className="print-title font-sans">Colombo Santino</h1>
          <p className="print-subtitle font-sans mt-1">Full-Stack Developer | 4th Year Computer Engineering Student</p>
          <div className="flex flex-wrap gap-4 mt-2 text-sm text-muted">
            <span>Pilar, Buenos Aires, Argentina</span>
            <span>•</span>
            <span>santinocolombo13@gmail.com</span>
            <span>•</span>
            <span>1140997531</span>
            <span>•</span>
            <a href="https://github.com/Santino2005" target="_blank" rel="noopener noreferrer">github.com/Santino2005</a>
            <span>•</span>
            <a href="https://www.linkedin.com/in/santino-colombo-a6259a304" target="_blank" rel="noopener noreferrer">linkedin.com/in/santino-colombo-a6259a304</a>
          </div>
        </div>

        <motion.div
          variants={containerVariants}
          initial="hidden"
          animate="visible"
          className="flex flex-col items-center gap-6 no-print"
        >
          {/* Badge */}
          <motion.div
            variants={itemVariants}
            className="inline-flex items-center gap-2 px-3 py-1 bg-primary/10 text-primary border border-primary/20 rounded-full text-xs font-semibold uppercase tracking-wider"
          >
            <span className="w-1.5 h-1.5 bg-primary rounded-full animate-ping" />
            Available for New Opportunities
          </motion.div>

          {/* Title */}
          <motion.h1
            variants={itemVariants}
            className="font-sans text-4xl sm:text-6xl md:text-7xl font-extrabold tracking-tight text-foreground"
          >
            Hi, I'm <span className="bg-gradient-to-r from-primary via-secondary to-accent bg-clip-text text-transparent">Colombo Santino</span>
          </motion.h1>

          {/* Subtitle */}
          <motion.h2
            variants={itemVariants}
            className="font-sans text-xl sm:text-2xl md:text-3xl font-semibold text-foreground/80 max-w-2xl"
          >
            Full-Stack Developer &amp; 4th Year Computer Engineering Student
          </motion.h2>

          {/* Description */}
          <motion.p
            variants={itemVariants}
            className="font-sans text-base sm:text-lg text-muted max-w-2xl leading-relaxed"
          >
            Passionate about software quality, robust systems architecture, and complex problem-solving.
            Specialized in the <strong className="font-semibold text-foreground">Java/Kotlin (Spring Boot)</strong> and <strong className="font-semibold text-foreground">TypeScript/React (Next.js)</strong> ecosystems, 
            applying engineering rigor through unit, integration, and TDD testing.
          </motion.p>

          {/* Action Buttons */}
          <motion.div
            variants={itemVariants}
            className="flex flex-wrap justify-center items-center gap-4 mt-4"
          >
            <a
              href="#contact"
              className="flex items-center gap-2 px-6 py-3 bg-primary text-white font-semibold rounded-full hover:bg-primary/95 hover:shadow-md hover:shadow-primary/10 transition-all duration-300 group"
            >
              Let's Talk
              <ArrowRight size={16} className="group-hover:translate-x-1 transition-transform" />
            </a>

            <a
              href="/Santino_Colombo-CV.pdf"
              download="Santino_Colombo-CV.pdf"
              className="flex items-center gap-2 px-6 py-3 bg-white text-foreground font-semibold border border-border rounded-full hover:bg-black/5 hover:border-foreground/20 transition-all duration-300"
            >
              <FileText size={18} className="text-primary" />
              Download CV
            </a>
          </motion.div>

          {/* Social Links */}
          <motion.div
            variants={itemVariants}
            className="flex items-center gap-5 mt-6"
          >
            <a
              href="https://github.com/Santino2005"
              target="_blank"
              rel="noopener noreferrer"
              className="p-2.5 bg-white border border-border text-muted hover:text-foreground hover:border-foreground/20 rounded-full shadow-sm hover:scale-105 transition-all duration-200"
              aria-label="GitHub"
            >
              <Github size={20} />
            </a>
            <a
              href="https://github.com/Santino2005/scolombo-portfolio"
              target="_blank"
              rel="noopener noreferrer"
              className="p-2.5 bg-white border border-border text-muted hover:text-foreground hover:border-foreground/20 rounded-full shadow-sm hover:scale-105 transition-all duration-200"
              aria-label="Portfolio Repo"
            >
              <span className="text-xs font-bold font-mono">Repo</span>
            </a>
            <a
              href="https://www.linkedin.com/in/santino-colombo-a6259a304"
              target="_blank"
              rel="noopener noreferrer"
              className="p-2.5 bg-white border border-border text-muted hover:text-foreground hover:border-foreground/20 rounded-full shadow-sm hover:scale-105 transition-all duration-200"
              aria-label="LinkedIn"
            >
              <Linkedin size={20} />
            </a>
            <a
              href="mailto:santinocolombo13@gmail.com"
              className="p-2.5 bg-white border border-border text-muted hover:text-foreground hover:border-foreground/20 rounded-full shadow-sm hover:scale-105 transition-all duration-200"
              aria-label="Email"
            >
              <Mail size={20} />
            </a>
          </motion.div>
        </motion.div>
      </div>
    </section>
  );
}
