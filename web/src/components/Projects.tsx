"use client";

import React, { useState } from "react";
import { FolderGit2, ExternalLink, ListChecks, Download } from "lucide-react";

export default function Projects() {
  const [expandedIndex, setExpandedIndex] = useState<number | null>(null);

  const projectsList = [
    {
      title: "Bravito - Modern Workflow & Startup Platform",
      category: "Full-Stack Project (SaaS)",
      importance: "Principal",
      description: "A cutting-edge web platform developed in 2026 designed to deliver optimized user workflows, featuring high-performance design, interactive components, and a robust scalable backend integration.",
      tech: ["Next.js", "TypeScript", "React", "Tailwind CSS", "Spring Boot", "PostgreSQL", "Docker"],
      features: [
        "Workflow Optimization: Tailored processes ensuring maximum speed and scalability.",
        "Interactive UX/UI: Modern glassmorphic interfaces designed under premium design standards.",
        "Backend Connectivity: Seamless connection with a secure API for real-time operations.",
      ],
      repo: "https://github.com/Santino2005/scolombo-portfolio",
      demo: "https://bravitoapp.vercel.app/login",
    },
    {
      title: "ZeroDebt - Expense Sharing & Debt Tracker",
      category: "Android Mobile App",
      importance: "Principal",
      description: "An Android application designed to help users manage and split shared expenses in a simple and transparent way. Keeps track of who paid for what and calculates how much each participant owes, making debt management effortless.",
      tech: ["Kotlin", "Jetpack Compose", "Material Design 3", "MVVM Architecture", "Navigation Compose", "ViewModel", "State Management"],
      features: [
        "Expense Tracking: Record shared expenses with descriptions, amounts, dates, and the person who paid.",
        "Group Management: Create and manage groups for trips, roommates, events, or any shared activity.",
        "Debt Calculation: Automatically calculate balances and display exactly who owes whom.",
        "Settlement Tracking: Mark debts as settled and keep a history of completed payments.",
      ],
      repo: "https://github.com/Santino2005/scolombo-portfolio/tree/main/MobileApp",
      demo: null,
      download: "/zeroDebt-SC.apk",
    },
    {
      title: "Tome - Library & Book Club Management Platform",
      category: "Full-Stack Project (SaaS)",
      importance: "Principal",
      description: "A comprehensive application for managing personal and public book collections and engaging in book clubs. Combines a high-performance frontend with a robust Kotlin backend using strict enterprise testing practices.",
      tech: ["Next.js (App Router)", "TypeScript", "React", "Tailwind CSS", "shadcn/ui", "Kotlin (JVM 21)", "Spring Boot 3.5.5", "Okta Spring Boot Starter", "PostgreSQL", "Apache Commons CSV", "Testcontainers (Postgres)", "JaCoCo", "ktlint"],
      features: [
        "Personal & Public Library: Search, filter, and add books with bulk uploading from CSV files.",
        "Book Clubs: Create, invite, subscribe, and share literary discussion rooms.",
        "OAuth2 Security: Native Auth0/Okta authentication integrated across frontend and backend.",
        "Quality Architecture: Extensive testing using Mockito and Testcontainers to validate databases in isolated Docker containers.",
      ],
      repo: "https://github.com/Santino2005/scolombo-portfolio/tree/main/Tome",
      demo: null,
    },
    {
      title: "UberClocked - Computing Hardware E-Commerce",
      category: "Full-Stack Project (E-Commerce)",
      importance: "Principal",
      description: "An e-commerce Web API and frontend SPA built to host a computer hardware catalog, complete with secure authentication flows and live checkout payment processing through MercadoPago.",
      tech: ["React 19", "Vite", "TypeScript", "Tailwind CSS v4", "shadcn/ui", "Java 21", "Spring Boot 4.0.1", "Spring Security (OAuth2)", "MercadoPago SDK (Java/React)", "PostgreSQL", "H2", "Storybook", "Vitest", "Playwright", "Checkstyle", "Spotless", "Spotbugs"],
      features: [
        "Payment Checkout: Direct MercadoPago SDK integration to process secure transactions.",
        "Auth0 Authentication: Secure login flow in frontend and JWT validation in backend.",
        "Storybook Environment: UI components documented and visually tested using Storybook.",
        "Strict Code Quality: Spotbugs for vulnerability checking, Checkstyle for standards, and Vitest/Playwright for testing.",
      ],
      repo: "https://github.com/Santino2005/scolombo-portfolio/tree/main/UberClocked",
      demo: null,
    },
    {
      title: "Baby-IO - IoT Baby Monitor System",
      category: "Hardware & IoT Project",
      importance: "High",
      description: "An electronics and software integration project. Reads physical sensors connected to an Arduino microcontroller to analyze sound, movement, and temperature, pushing real-time status updates to a mobile app.",
      tech: ["Arduino C++", "Java 17", "Spring Boot 3.4.4", "Spring Integration MQTT", "WebSockets (STOMP)", "React 19", "Capacitor (Android)", "PostgreSQL"],
      features: [
        "Sensory Monitoring: Sound, motion, and temperature reading using physical sensors on Arduino.",
        "MQTT Ingestion: Sensor metrics sent to a Spring Boot backend via MQTT protocol.",
        "Real-Time Push: WebSocket/STOMP connection to deliver instant state updates to caregivers.",
        "Native Android App: React frontend packaged with Capacitor for native mobile execution.",
      ],
      repo: "https://github.com/Santino2005/scolombo-portfolio/tree/main/Baby-IO",
      demo: null,
    },
    {
      title: "Control de Ingresos PRO - Visitor Access Control",
      category: "Modular Monolith (Technical Challenge)",
      importance: "High",
      description: "A modular monolith built for the Geno Insights technical challenge. Migrated a local-only app into a centralized visitor access database, featuring Supabase photo uploads and QR token validation.",
      tech: ["React 19", "Vite", "JavaScript", "html5-qrcode", "qrcode.react", "Java", "Spring Boot", "Spring Data JPA", "PostgreSQL", "Supabase Storage", "Railway", "Excel Export"],
      features: [
        "Visitor Registration: Capture personal details (DNI, company, sector) with photo stored on Supabase Storage.",
        "QR Code Generator & Scanner: Unique QR credentials generated per visit, scanned via web camera to validate entries/exits.",
        "Guard Login: Basic authentication using BCrypt for hashed PIN security.",
        "Excel Exporting: Daily and historic visitor log reports exported directly to Excel spreadsheets.",
      ],
      repo: "https://github.com/Santino2005/scolombo-portfolio/tree/main/OtherChallenges/geno-Insights-challenge",
      demo: "https://geno-insights-challenge.vercel.app",
    },
    {
      title: "Email Service - API Sirius Challenge",
      category: "Resilient Microservice",
      importance: "Medium",
      description: "A highly available REST API for sending transaction and marketing emails. Ensures high deliverability using automated provider failover logic.",
      tech: ["Java", "Spring Boot", "Spring Security", "Auth0 (JWT)", "SendGrid API", "SparkPost API", "PostgreSQL", "Docker", "Docker Compose"],
      features: [
        "Active Failover (Fallback): Routes emails via SparkPost (primary) and dynamically redirects to SendGrid (fallback) if SparkPost fails.",
        "Role-Based Security: Auth0 JWT validation with roles ('user', 'admin') and custom claims.",
        "Containerized: Easy setups and local runs using Docker Compose configurations.",
      ],
      repo: "https://github.com/Santino2005/scolombo-portfolio/tree/main/OtherChallenges/EmailService-Challenge",
      demo: null,
    },
    {
      title: "Advanced Data Structures & Algorithms",
      category: "Academic Library (Universidad Austral)",
      importance: "Educational",
      description: "A comprehensive repository containing data structure implementations, sorting engines, and advanced search and compression algorithms.",
      tech: ["Java", "Gradle", "JUnit 5", "JaCoCo", "TeamCity CI"],
      features: [
        "Advanced Structures: Self-balancing BST trees (Red-Black Trees, Randomized BST).",
        "Compression Algorithms: Huffman Coding, Burrows-Wheeler Transform, Run-Length Encoding (RLE), and Move-To-Front.",
        "Search & Tries: Ternary Search Tries (TST), R-Way Tries, and string matching (Rabin-Karp).",
        "Sorting Engines: Empirical testing of QuickSort, MergeSort, ShellSort, etc., using an Observable design.",
      ],
      repo: "https://github.com/Santino2005/scolombo-portfolio/tree/main/Algorithms",
      demo: null,
    },
    {
      title: "MyChessWithUI - Chess Engine",
      category: "Academic System Design (Universidad Austral)",
      importance: "Educational",
      description: "Development of a highly modular, decoupled chess engine applying object-oriented design patterns in a mixed Java and Kotlin codebase.",
      tech: ["Java 21", "Kotlin", "OOP Design Patterns", "JUnit 5", "Gradle"],
      features: [
        "Decoupled Architecture: Clean separation between logical board rules and GUI renderers.",
        "Complete Chess Rules: Check, checkmate, castling, pawn promotion, and double-step validations.",
        "OOP Design: Implementation of abstract factories and polymorphic path validators.",
      ],
      repo: "https://github.com/Santino2005/scolombo-portfolio/tree/main/MyChessWithUI",
      demo: null,
    },
    {
      title: "Immutable TaTeTi (Tic Tac Toe)",
      category: "Academic Project (Universidad Austral)",
      importance: "Educational",
      description: "A mathematical implementation of Tic Tac Toe following the functional paradigm of total immutability, ensuring auditing safety.",
      tech: ["Java 21", "Gradle", "JaCoCo (85%+ Coverage)", "Spotless", "Checkstyle"],
      features: [
        "State Immutability: Recreates game board representations on every turn to prevent side effects.",
        "Style Auditing: Google Java Format enforced through Checkstyle linter and Spotless formatter.",
      ],
      repo: "https://github.com/Santino2005/scolombo-portfolio/tree/main/InmutableTicTacToe",
      demo: null,
    },
  ];

  return (
    <section id="projects" className="py-24 bg-background scroll-mt-10">
      <div className="max-w-5xl mx-auto px-6">
        {/* Section Title */}
        <div className="text-center mb-16">
          <h2 className="font-sans text-xs font-semibold text-primary uppercase tracking-widest mb-3">Portfolio</h2>
          <p className="font-sans text-3xl sm:text-4xl font-extrabold text-foreground">Developed Projects</p>
          <div className="w-12 h-1 bg-primary mx-auto mt-4 rounded-full" />
        </div>

        {/* Desktop / Web Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8 no-print">
          {projectsList.map((project, index) => (
            <div
              key={index}
              className="bg-white border border-border/40 rounded-2xl p-6 shadow-sm flex flex-col justify-between hover:shadow-md hover:border-primary/10 transition-all duration-300 relative overflow-hidden group"
            >
              {/* Importance Tag */}
              <div className="absolute top-0 right-0 bg-primary/10 text-primary text-[10px] uppercase tracking-wider font-extrabold px-3 py-1 rounded-bl-xl border-l border-b border-primary/5">
                {project.importance}
              </div>

              <div>
                <p className="font-mono text-xs font-bold text-accent tracking-wider uppercase mb-1">
                  {project.category}
                </p>
                <h3 className="font-sans text-lg font-bold text-foreground mb-3 leading-tight group-hover:text-primary transition-colors">
                  {project.title}
                </h3>
                <p className="font-sans text-sm text-muted mb-4 leading-relaxed line-clamp-3">
                  {project.description}
                </p>

                {/* Tech Chips */}
                <div className="flex flex-wrap gap-1.5 mb-5">
                  {project.tech.slice(0, 5).map((t, idx) => (
                    <span
                      key={idx}
                      className="font-sans text-[10px] font-semibold px-2 py-0.5 bg-background border border-border/50 text-foreground/80 rounded"
                    >
                      {t}
                    </span>
                  ))}
                  {project.tech.length > 5 && (
                    <span className="font-sans text-[10px] font-semibold px-2 py-0.5 bg-primary/5 border border-primary/10 text-primary rounded">
                      +{project.tech.length - 5} more
                    </span>
                  )}
                </div>
              </div>

              {/* Collapsible Features & Links */}
              <div className="border-t border-border/40 pt-4 flex flex-col gap-3">
                {expandedIndex === index ? (
                  <div className="bg-background/50 border border-border/30 p-3 rounded-lg animate-fade-in mb-1">
                    <p className="text-xs font-bold text-foreground flex items-center gap-1.5 mb-2">
                      <ListChecks size={14} className="text-primary" /> Key features:
                    </p>
                    <ul className="list-disc list-inside text-[11px] text-muted space-y-1.5 leading-relaxed">
                      {project.features.map((feat, fIdx) => (
                        <li key={fIdx}>{feat}</li>
                      ))}
                    </ul>
                  </div>
                ) : null}

                <div className="flex items-center justify-between">
                  <button
                    onClick={() => setExpandedIndex(expandedIndex === index ? null : index)}
                    className="text-xs font-bold text-primary hover:text-primary/80 transition-colors flex items-center gap-1"
                  >
                    {expandedIndex === index ? "Hide details" : "View details & features"}
                  </button>

                  <div className="flex items-center gap-3">
                    <a
                      href={project.repo}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-xs font-bold text-muted hover:text-foreground flex items-center gap-1 transition-colors"
                    >
                      <FolderGit2 size={14} />
                      Repo
                    </a>
                    {project.demo ? (
                      <a
                        href={project.demo}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="text-xs font-bold text-accent hover:text-accent/80 flex items-center gap-1 transition-colors"
                      >
                        <ExternalLink size={14} />
                        Demo
                      </a>
                    ) : null}
                    {"download" in project && project.download ? (
                      <a
                        href={project.download}
                        download
                        className="text-xs font-bold text-accent hover:text-accent/80 flex items-center gap-1 transition-colors"
                      >
                        <Download size={14} />
                        Download APK
                      </a>
                    ) : null}
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Print-only layout for CV compilation */}
        <div className="hidden print-only print-section">
          <h2 className="text-lg font-bold uppercase tracking-wider text-primary border-b pb-1 mb-4">Featured Projects</h2>
          <div className="space-y-6">
            {projectsList.slice(0, 5).map((project, index) => (
              <div key={index} className="timeline-item">
                <div className="flex justify-between items-baseline font-bold">
                  <span className="text-sm">{project.title}</span>
                  <span className="text-xs text-muted font-normal">{project.category}</span>
                </div>
                <p className="text-xs text-muted mt-1">{project.description}</p>
                <p className="text-xs font-semibold mt-1">Tech Stack: <span className="font-normal text-muted">{project.tech.join(", ")}</span></p>
                <div className="mt-1">
                  <p className="text-xs font-semibold">Key Achievements:</p>
                  <ul className="list-disc list-inside text-xs text-muted pl-2 mt-0.5 space-y-0.5">
                    {project.features.map((feat, fIdx) => (
                      <li key={fIdx}>{feat}</li>
                    ))}
                  </ul>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}
