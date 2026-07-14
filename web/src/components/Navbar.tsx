"use client";

import React, { useState, useEffect } from "react";
import { Menu, X, FileText, Github, Linkedin, Mail } from "lucide-react";

export default function Navbar() {
  const [isOpen, setIsOpen] = useState(false);
  const [scrolled, setScrolled] = useState(false);
  const [scrollProgress, setScrollProgress] = useState(0);

  useEffect(() => {
    const handleScroll = () => {
      const totalScroll = document.documentElement.scrollHeight - window.innerHeight;
      if (totalScroll > 0) {
        setScrollProgress((window.scrollY / totalScroll) * 100);
      }
      setScrolled(window.scrollY > 20);
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  const navLinks = [
    { name: "About Me", href: "#about-me" },
    { name: "Projects", href: "#projects" },
    { name: "Timeline", href: "#timeline" },
    { name: "Skills", href: "#skills" },
    { name: "Stats", href: "#stats" },
    { name: "Contact", href: "#contact" },
  ];

  return (
    <nav
      className={`fixed top-0 left-0 right-0 z-50 transition-all duration-300 no-print ${
        scrolled ? "glass shadow-sm py-3" : "bg-transparent py-5"
      }`}
    >
      {/* Scroll Progress Bar */}
      <div
        className="absolute top-0 left-0 h-[3px] bg-primary transition-all duration-100"
        style={{ width: `${scrollProgress}%` }}
      />

      <div className="max-w-6xl mx-auto px-6 flex justify-between items-center">
        {/* Logo */}
        <a href="#" className="font-sans font-bold text-xl tracking-tight text-foreground flex items-center gap-2 group">
          <span className="bg-primary text-white w-8 h-8 rounded-lg flex items-center justify-center font-mono text-base shadow-sm group-hover:scale-105 transition-transform">
            CS
          </span>
          <span className="hidden sm:inline-block font-sans group-hover:text-primary transition-colors">
            Colombo Santino
          </span>
        </a>

        {/* Desktop Links */}
        <div className="hidden md:flex items-center gap-6">
          {navLinks.map((link) => (
            <a
              key={link.name}
              href={link.href}
              className="text-sm font-medium text-muted hover:text-primary transition-colors duration-200"
            >
              {link.name}
            </a>
          ))}
          <a
            href="/Santino_Colombo-CV.pdf"
            download="Santino_Colombo-CV.pdf"
            className="flex items-center gap-1.5 px-4 py-2 text-xs font-semibold uppercase tracking-wider text-primary border border-primary/20 rounded-full hover:bg-primary hover:text-white transition-all duration-300 shadow-sm"
          >
            <FileText size={14} />
            CV
          </a>
        </div>

        {/* Mobile Menu Button */}
        <button
          onClick={() => setIsOpen(!isOpen)}
          className="md:hidden p-1.5 rounded-lg text-muted hover:text-foreground hover:bg-black/5 transition-colors"
        >
          {isOpen ? <X size={22} /> : <Menu size={22} />}
        </button>
      </div>

      {/* Mobile Links Panel */}
      {isOpen && (
        <div className="md:hidden absolute top-full left-0 right-0 bg-surface/95 border-b border-primary/5 py-4 px-6 flex flex-col gap-4 shadow-lg backdrop-blur-md animate-fade-in">
          {navLinks.map((link) => (
            <a
              key={link.name}
              href={link.href}
              onClick={() => setIsOpen(false)}
              className="text-base font-semibold text-foreground/80 hover:text-primary transition-colors py-1"
            >
              {link.name}
            </a>
          ))}
          <a
            href="/Santino_Colombo-CV.pdf"
            download="Santino_Colombo-CV.pdf"
            onClick={() => setIsOpen(false)}
            className="flex items-center justify-center gap-2 w-full mt-2 px-4 py-2.5 text-sm font-semibold text-primary border border-primary/20 rounded-full hover:bg-primary hover:text-white transition-all duration-300 shadow-sm"
          >
            <FileText size={16} />
            Download CV
          </a>
        </div>
      )}
    </nav>
  );
}
