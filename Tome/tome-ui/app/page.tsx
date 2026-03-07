import { auth0 } from '@/lib/auth0';
import { HomePage } from '@/components/pages/HomePage';
import { LandingPage } from '@/components/pages/LandingPage';

export default async function Index() {
  const session = await auth0.getSession();

  if (session) {
    return <HomePage />;
  } else {
    return <LandingPage />;
  }
}
