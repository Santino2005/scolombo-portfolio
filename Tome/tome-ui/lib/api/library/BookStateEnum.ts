enum BookState {
  READ = 'READ',
  READING = 'READING',
  DNF = 'DNF',
  WANT_TO_READ = 'WANT_TO_READ',
}

export const BookStateNames: Record<string, BookState> = {
  READ: BookState.READ,
  READING: BookState.READING,
  DNF: BookState.DNF,
  WANT_TO_READ: BookState.WANT_TO_READ,
};

export default BookState;
