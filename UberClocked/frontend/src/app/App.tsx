import { useEffect } from "react";
import { initMercadoPago } from "@mercadopago/sdk-react";
import { RouterProvider } from "react-router-dom";
import { router } from "./routes/router";
import { useAuth0 } from "@auth0/auth0-react";

function App() {
  const { isAuthenticated, getAccessTokenSilently } = useAuth0();

  useEffect(() => {
    try {
      initMercadoPago("TEST-dbeefbf1-09b0-4f59-97de-d4575669c873");
    } catch {
      // Ignore if offline
    }
  }, [isAuthenticated, getAccessTokenSilently]);

  return (
    <RouterProvider
      router={router}
      fallbackElement={
        <div className="min-h-screen bg-zinc-950 flex flex-col items-center justify-center text-white space-y-3">
          <div className="w-10 h-10 border-3 border-orange-500 border-t-transparent rounded-full animate-spin" />
          <p className="text-xs font-black uppercase tracking-widest text-orange-400">Loading ÜberClocked...</p>
        </div>
      }
    />
  );
}

export default App;
