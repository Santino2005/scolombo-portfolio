import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Colombo Santino | Full-Stack Developer & Computer Engineering Student",
  description: "Personal portfolio and online CV of Colombo Santino, a 4th-year Computer Engineering student and Full-Stack Developer specializing in Java, Kotlin, Spring Boot, React, Next.js, and TypeScript.",
  keywords: [
    "Colombo Santino",
    "Santino Colombo",
    "Full-Stack Developer",
    "Software Engineer",
    "Computer Engineering Student",
    "Universidad Austral",
    "Java",
    "Kotlin",
    "Spring Boot",
    "React",
    "Next.js",
    "TypeScript",
    "Portfolio",
    "CV Online"
  ],
  authors: [{ name: "Colombo Santino", url: "https://github.com/Santino2005" }],
  creator: "Colombo Santino",
  openGraph: {
    title: "Colombo Santino | Full-Stack Developer & Computer Engineering Student",
    description: "Personal portfolio and online CV of Colombo Santino. Discover his projects, skills, academic background, and technical expertise.",
    url: "https://scolombo-portfolio.vercel.app",
    siteName: "Colombo Santino Portfolio",
    locale: "en_US",
    type: "website",
  },
  robots: {
    index: true,
    follow: true,
    googleBot: {
      index: true,
      follow: true,
      "max-video-preview": -1,
      "max-image-preview": "large",
      "max-snippet": -1,
    },
  },
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html
      lang="en"
      className={`${geistSans.variable} ${geistMono.variable} h-full scroll-smooth antialiased`}
    >
      <body className="min-h-full flex flex-col bg-background text-foreground selection:bg-primary/20 selection:text-primary">
        {children}
      </body>
    </html>
  );
}

