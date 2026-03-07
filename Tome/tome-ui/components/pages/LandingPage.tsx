'use client';

import NavBar from '../landing/navbar/Navbar';
import HomeSection from '../landing/sections/home/HomeSection';
import OverviewSection from '../landing/sections/overview/OverviewSection';
import FeatureSection from '../landing/sections/features/FeatureSection';

export function LandingPage() {
  return (
    <>
      <NavBar />
      <HomeSection />
      <OverviewSection />
      <FeatureSection />
    </>
  );
}
