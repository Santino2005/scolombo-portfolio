import { auth0 } from '@/lib/auth0';
import { NextResponse } from 'next/server';

const API_URL = process.env.API_URL;

export async function DELETE(request: Request, context: { params: Promise<{ bookId: string }> }) {
  try {
    const { bookId } = await context.params;

    const { token } = await auth0.getAccessToken();

    const backendUrl = `${API_URL}/libraries/books/${bookId}`;

    const response = await fetch(backendUrl, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      const errorText = await response.text();
      console.error('Error desde el backend:', errorText);
      return NextResponse.json(
        { message: `Error del backend: ${response.status}`, details: errorText },
        { status: response.status },
      );
    }

    return NextResponse.json({ message: 'Book deleted successfully' }, { status: 200 });
  } catch (error) {
    console.error('Error en la API Route DELETE:', error);
    return NextResponse.json(
      {
        message: 'Error interno en el servidor de Next.js',
        details: error instanceof Error ? error.message : String(error),
      },
      { status: 500 },
    );
  }
}
