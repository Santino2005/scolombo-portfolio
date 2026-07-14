"use client";

import React, { useState } from "react";
import { Mail, Phone, MapPin, Send, Github, Linkedin } from "lucide-react";

export default function Contact() {
  const [form, setForm] = useState({ name: "", email: "", message: "" });
  const [status, setStatus] = useState<"idle" | "sending" | "success" | "error">("idle");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.name || !form.email || !form.message) {
      setStatus("error");
      return;
    }

    setStatus("sending");

    const emailTo = "santinocolombo13@gmail.com";
    const subject = encodeURIComponent(`Portfolio Contact from ${form.name}`);
    const body = encodeURIComponent(
      `Hello Santino,\n\nMy name is ${form.name}.\n\nMessage:\n${form.message}`
    );

    // Direct Google Mail Compose URL
    const gmailUrl = `https://mail.google.com/mail/?view=cm&fs=1&to=${emailTo}&su=${subject}&body=${body}`;

    // Redirect to Gmail compose in a new tab
    window.open(gmailUrl, "_blank");

    setStatus("success");
    setForm({ name: "", email: "", message: "" });
  };

  return (
    <section id="contact" className="py-24 bg-white border-t border-border/40 scroll-mt-10">
      <div className="max-w-5xl mx-auto px-6">
        {/* Section Title */}
        <div className="text-center mb-16">
          <h2 className="font-sans text-xs font-semibold text-primary uppercase tracking-widest mb-3">Connection</h2>
          <p className="font-sans text-3xl sm:text-4xl font-extrabold text-foreground">Contact</p>
          <div className="w-12 h-1 bg-primary mx-auto mt-4 rounded-full" />
        </div>

        <div className="grid grid-cols-1 md:grid-cols-12 gap-12 items-stretch">
          {/* Contact Details Column */}
          <div className="md:col-span-5 flex flex-col justify-between">
            <div className="space-y-6">
              <h3 className="font-sans text-xl font-bold text-foreground">
                Let's work together
              </h3>
              <p className="font-sans text-sm text-muted leading-relaxed">
                If you are looking for someone with an analytical mindset, obsessed with software quality, and capable of designing both scalable backend services and attractive frontends, drop me a message!
              </p>
            </div>

            <div className="space-y-5 my-8">
              <div className="flex items-center gap-4">
                <div className="p-3 bg-primary/5 text-primary rounded-xl shrink-0">
                  <Mail size={20} />
                </div>
                <div>
                  <p className="text-xs font-semibold text-muted tracking-wider uppercase">Email</p>
                  <a href="mailto:santinocolombo13@gmail.com" className="text-sm font-bold text-foreground hover:text-primary transition-colors">
                    santinocolombo13@gmail.com
                  </a>
                </div>
              </div>

              <div className="flex items-center gap-4">
                <div className="p-3 bg-primary/5 text-primary rounded-xl shrink-0">
                  <Phone size={20} />
                </div>
                <div>
                  <p className="text-xs font-semibold text-muted tracking-wider uppercase">Phone</p>
                  <a href="tel:1140997531" className="text-sm font-bold text-foreground hover:text-primary transition-colors">
                    +54 11 4099-7531
                  </a>
                </div>
              </div>

              <div className="flex items-center gap-4">
                <div className="p-3 bg-primary/5 text-primary rounded-xl shrink-0">
                  <MapPin size={20} />
                </div>
                <div>
                  <p className="text-xs font-semibold text-muted tracking-wider uppercase">Location</p>
                  <p className="text-sm font-bold text-foreground">
                    Pilar, Buenos Aires, Argentina
                  </p>
                </div>
              </div>
            </div>

            {/* Quick action social links */}
            <div className="flex items-center gap-4 no-print">
              <a
                href="https://github.com/Santino2005"
                target="_blank"
                rel="noopener noreferrer"
                className="flex items-center gap-2 px-4 py-2.5 bg-background border border-border/50 text-sm font-semibold rounded-xl text-muted hover:text-foreground hover:border-foreground/20 transition-all shadow-sm"
              >
                <Github size={18} />
                GitHub
              </a>
              <a
                href="https://www.linkedin.com/in/santino-colombo-a6259a304"
                target="_blank"
                rel="noopener noreferrer"
                className="flex items-center gap-2 px-4 py-2.5 bg-background border border-border/50 text-sm font-semibold rounded-xl text-muted hover:text-foreground hover:border-foreground/20 transition-all shadow-sm"
              >
                <Linkedin size={18} />
                LinkedIn
              </a>
            </div>
          </div>

          {/* Contact Form Column */}
          <div className="md:col-span-7 bg-background border border-border/40 p-6 sm:p-8 rounded-3xl shadow-sm flex flex-col justify-between no-print">
            <form onSubmit={handleSubmit} className="space-y-5">
              <div className="space-y-1.5">
                <label htmlFor="name" className="text-xs font-bold text-foreground/80 tracking-wider uppercase">
                  Full Name
                </label>
                <input
                  type="text"
                  id="name"
                  value={form.name}
                  onChange={(e) => setForm({ ...form, name: e.target.value })}
                  placeholder="John Doe"
                  className="w-full px-4 py-3 bg-white border border-border/50 rounded-xl text-sm focus:outline-none focus:border-primary/40 focus:ring-1 focus:ring-primary/20 transition-all text-foreground"
                />
              </div>

              <div className="space-y-1.5">
                <label htmlFor="email" className="text-xs font-bold text-foreground/80 tracking-wider uppercase">
                  Email Address
                </label>
                <input
                  type="email"
                  id="email"
                  value={form.email}
                  onChange={(e) => setForm({ ...form, email: e.target.value })}
                  placeholder="john@example.com"
                  className="w-full px-4 py-3 bg-white border border-border/50 rounded-xl text-sm focus:outline-none focus:border-primary/40 focus:ring-1 focus:ring-primary/20 transition-all text-foreground"
                />
              </div>

              <div className="space-y-1.5">
                <label htmlFor="message" className="text-xs font-bold text-foreground/80 tracking-wider uppercase">
                  Message
                </label>
                <textarea
                  id="message"
                  value={form.message}
                  onChange={(e) => setForm({ ...form, message: e.target.value })}
                  rows={4}
                  placeholder="Hi, I'd like to talk about..."
                  className="w-full px-4 py-3 bg-white border border-border/50 rounded-xl text-sm focus:outline-none focus:border-primary/40 focus:ring-1 focus:ring-primary/20 transition-all text-foreground resize-none"
                />
              </div>

              <button
                type="submit"
                disabled={status === "sending"}
                className="w-full flex items-center justify-center gap-2 px-6 py-3.5 bg-primary text-white font-semibold rounded-xl hover:bg-primary/95 hover:shadow-md hover:shadow-primary/10 transition-all duration-300 disabled:opacity-50"
              >
                {status === "sending" ? (
                  "Sending..."
                ) : (
                  <>
                    Send Message
                    <Send size={16} />
                  </>
                )}
              </button>

              {status === "success" && (
                <div className="p-3.5 bg-accent/10 border border-accent/20 text-accent text-xs font-semibold rounded-xl animate-fade-in text-center">
                  Message sent successfully! I will get in touch soon.
                </div>
              )}

              {status === "error" && (
                <div className="p-3.5 bg-red-500/10 border border-red-500/20 text-red-500 text-xs font-semibold rounded-xl animate-fade-in text-center">
                  Please fill out all fields in the form.
                </div>
              )}
            </form>
          </div>
        </div>

        {/* Print-only contact details */}
        <div className="hidden print-only print-section">
          <h2 className="text-lg font-bold uppercase tracking-wider text-primary border-b pb-1 mb-4">Contact Information</h2>
          <div className="grid grid-cols-2 gap-4 text-sm">
            <div><strong>Email:</strong> santinocolombo13@gmail.com</div>
            <div><strong>Phone:</strong> +54 11 4099-7531</div>
            <div><strong>Location:</strong> Pilar, Buenos Aires, Argentina</div>
            <div><strong>GitHub:</strong> github.com/Santino2005</div>
            <div><strong>LinkedIn:</strong> linkedin.com/in/santino-colombo-a6259a304</div>
          </div>
        </div>
      </div>
    </section>
  );
}
