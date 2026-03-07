export async function ReadingStatusMocking(
  bookId: string,
  status: 'READING' | 'NOT_READING',
): Promise<boolean> {
  console.log(`Simulando actualización para el libro ${bookId} al estado ${status}...`);

  await new Promise((resolve) => setTimeout(resolve, 1000));

  console.log('Simulación exitosa.');
  return true;
}
