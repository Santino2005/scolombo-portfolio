import type { BookData } from '@/lib/types/BookData';

export interface BookSections {
  inProgress: BookData[];
  wantToRead: BookData[];
  read: BookData[];
  DNF: BookData[];
}

export const LibraryProgressMocker: BookSections = {
  inProgress: [
    {
      id: '1',
      title: 'The Fellowship of the Ring',
      authors: [{ id: '1', fullName: 'J.R.R.', surname: 'Tolkien' }],
      url: 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1651340688i/727798.jpg',
      releaseDate: '1954',
      pages: 407,
      publisher: { id: '1', name: 'William Morrow' },
      synopsis:
        "The first volume in J.R.R. Tolkien's epic adventure THE LORD OF THE RINGS. One Ring to rule them all, One Ring to find them, One Ring to bring them all and in the darkness bind them. In ancient times the Rings of Power were crafted by the Elven-smiths, and Sauron, the Dark Lord, forged the One Ring, filling it with his own power so that he could rule all others. But the One Ring was taken from him, and though he sought it throughout Middle-earth, it remained lost to him. After many ages it fell into the hands of Bilbo Baggins, as told in The Hobbit. In a sleepy village in the Shire, young Frodo Baggins finds himself faced with an immense task, as his elderly cousin Bilbo entrusts the Ring to his care. Frodo must leave his home and make a perilous journey across Middle-earth to the Cracks of Doom, there to destroy the Ring and foil the Dark Lord in his evil purpose.",
      tags: [
        { id: 't1', name: 'Fantasy' },
        { id: 't2', name: 'Classics' },
        { id: 't3', name: 'Fiction' },
        { id: 't4', name: 'Adventure' },
        { id: 't5', name: 'High Fantasy' },
        { id: 't6', name: 'Epic Fantasy' },
      ],
      language: { id: 'en', name: 'English' },
    },
    {
      id: '4',
      title: 'My Friends',
      authors: [{ id: '2', fullName: 'Fredrik', surname: 'Backman' }],
      url: 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1734625930i/217163697.jpg',
      releaseDate: '2025',
      pages: 436,
      publisher: { id: '2', name: 'Atria Books' },
      synopsis:
        "The world is full of miracles, but none greater than how far a young person can be carried by someone else's belief in them. Most people don't even notice them—three tiny figures sitting at the end of a long pier in the corner of one of the most famous paintings in the world. Most people think it's just a depiction of a wide expanse of sea. But Louisa, soon to be eighteen years old and an aspiring artist herself, knows otherwise. She is determined to find out the story behind these three enigmatic figures.",
      tags: [
        { id: 't7', name: 'Fiction' },
        { id: 't8', name: 'Contemporary' },
        { id: 't9', name: 'Literary Fiction' },
        { id: 't10', name: 'Book Club' },
        { id: 't11', name: 'Adult' },
        { id: 't12', name: 'Friendship' },
      ],
      language: { id: 'en', name: 'English' },
    },
    {
      id: '10',
      title: 'Dune',
      authors: [{ id: '3', fullName: 'Frank', surname: 'Herbert' }],
      url: 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1555447414i/44767458.jpg',
      releaseDate: '1965',
      pages: 658,
      publisher: { id: '3', name: 'Ace' },
      synopsis:
        "Set on the desert planet Arrakis, Dune is the story of the boy Paul Atreides, heir to a noble family tasked with ruling an inhospitable world where the only thing of value is the 'spice' melange, a drug capable of extending life and enhancing consciousness. Coveted across the known universe, melange is a prize worth killing for...",
      tags: [
        { id: 't13', name: 'Science Fiction' },
        { id: 't14', name: 'Fiction' },
        { id: 't15', name: 'Fantasy' },
        { id: 't16', name: 'Classics' },
        { id: 't17', name: 'Adventure' },
        { id: 't18', name: 'Novel' },
      ],
      language: { id: 'en', name: 'English' },
    },
  ],
  wantToRead: [
    {
      id: '2',
      title: 'The Two Towers',
      authors: [{ id: '1', fullName: 'J.R.R.', surname: 'Tolkien' }],
      url: 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1629308732i/727800.jpg',
      releaseDate: '1954',
      pages: 448,
      publisher: { id: '1', name: 'William Morrow' },
      synopsis:
        "Begin your journey into Middle-earth. The inspiration for the upcoming original series on Prime Video, The Lord of the Rings: The Rings of Power. The Two Towers is the second part of J.R.R. Tolkien's epic adventure The Lord of the Rings. One Ring to rule them all, One Ring to find them, One Ring to bring them all and in the darkness bind them. Frodo and his Companions of the Ring have been beset by danger during their quest to prevent the Ruling Ring from falling into the hands of the Dark Lord by destroying it in the Cracks of Doom.",
      tags: [
        { id: 't1', name: 'Fantasy' },
        { id: 't2', name: 'Classics' },
        { id: 't3', name: 'Fiction' },
        { id: 't4', name: 'Adventure' },
        { id: 't5', name: 'High Fantasy' },
        { id: 't6', name: 'Epic Fantasy' },
      ],
      language: { id: 'en', name: 'English' },
    },
    {
      id: '7',
      title: 'Tress of the Emerald Sea',
      authors: [{ id: '3', fullName: 'Brandon', surname: 'Sanderson' }],
      url: 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1672574587i/60531406.jpg',
      releaseDate: '2023',
      pages: 443,
      publisher: { id: '3', name: 'Dragonsteel Entertainment' },
      synopsis:
        'The only life Tress has known on her island home in an emerald-green ocean has been a simple one, with the mysterious spore seas surrounding her and the occasional letter from Charlie, whose father takes him on long trading journeys to far-off places. But when his letters suddenly stop coming, Tress is determined to find out why...',
      tags: [
        { id: 't19', name: 'Fantasy' },
        { id: 't3', name: 'Fiction' },
        { id: 't10', name: 'Book Club' },
        { id: 't4', name: 'Adventure' },
        { id: 't5', name: 'High Fantasy' },
        { id: 't11', name: 'Adult' },
      ],
      language: { id: 'en', name: 'English' },
    },
    {
      id: '5',
      title: 'Beartown',
      authors: [{ id: '2', fullName: 'Fredrik', surname: 'Backman' }],
      url: 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1525349525i/31443394.jpg',
      releaseDate: '2017',
      pages: 418,
      publisher: { id: '2', name: 'Atria Books' },
      synopsis:
        'People say Beartown is finished. A tiny community nestled deep in the forest, it is slowly losing ground to the ever-encroaching trees. But down by the lake stands an old ice rink, built generations ago by the working men who founded this town. And in that ice rink is the reason people in Beartown believe tomorrow will be better than today. Their junior ice hockey team is about to compete in the national semi-finals, and they actually have a shot at winning.',
      tags: [
        { id: 't7', name: 'Fiction' },
        { id: 't8', name: 'Contemporary' },
        { id: 't9', name: 'Literary Fiction' },
        { id: 't10', name: 'Book Club' },
        { id: 't11', name: 'Adult' },
        { id: 't12', name: 'Friendship' },
      ],
      language: { id: 'en', name: 'English' },
    },
    {
      id: '11',
      title: 'Dune Messiah',
      authors: [{ id: '3', fullName: 'Frank', surname: 'Herbert' }],
      url: 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1577043824i/44492285.jpg',
      releaseDate: '1969',
      pages: 336,
      publisher: { id: '3', name: 'Ace' },
      synopsis:
        "Dune Messiah continues the story of Paul Atreides, better known--and feared--as the man christened Muad'Dib. As Emperor of the known universe, he possesses more power than a single man was ever meant to wield. Worshipped as a religious icon by the fanatical Fremen, Paul faces the enmity of the political houses he displaced when he assumed the throne--and a conspiracy conducted within his own sphere of influence.",
      tags: [
        { id: 't13', name: 'Science Fiction' },
        { id: 't14', name: 'Fiction' },
        { id: 't15', name: 'Fantasy' },
        { id: 't16', name: 'Classics' },
        { id: 't17', name: 'Adventure' },
        { id: 't18', name: 'Novel' },
      ],
      language: { id: 'en', name: 'English' },
    },
  ],
  read: [
    {
      id: '3',
      title: 'The Return of the King',
      authors: [{ id: '1', fullName: 'J.R.R.', surname: 'Tolkien' }],
      url: 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1634055544i/727810.jpg',
      releaseDate: '1955',
      pages: 432,
      publisher: { id: '1', name: 'William Morrow' },
      synopsis:
        'The concluding volume of The Lord of the Rings, telling of the opposing strategies of the wizard Gandalf and the Dark Lord as they struggle for possession of the Ring. The Return of the King tells of the final confrontation between the wizard Gandalf and his great enemy, the Dark Lord Sauron, in a battle to decide the fate of Middle-earth. It also tells the tale of the mission of the Ringbearer, Frodo, and his companion Sam as they journey into the heart of the Land of Shadow in a final desperate attempt to destroy the Ring in the fires of Mount Doom.',
      tags: [
        { id: 't1', name: 'Fantasy' },
        { id: 't2', name: 'Classics' },
        { id: 't3', name: 'Fiction' },
        { id: 't4', name: 'Adventure' },
        { id: 't5', name: 'High Fantasy' },
        { id: 't6', name: 'Epic Fantasy' },
      ],
      language: { id: 'en', name: 'English' },
    },
    {
      id: '8',
      title: 'Yumi and the Nightmare Painter',
      authors: [{ id: '3', fullName: 'Brandon', surname: 'Sanderson' }],
      url: 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1689135481i/60531416.jpg',
      releaseDate: '2023',
      pages: 480,
      publisher: { id: '3', name: 'Dragonsteel Entertainment' },
      synopsis:
        "Yumi comes from a land of gardens, meditation, and spirits, while Painter lives in a world of darkness, technology, and nightmares. When they inexplicably begin sharing each other's bodies, they discover that their worlds are not as separate as they believed, and that they may be the key to saving both of their peoples.",
      tags: [
        { id: 't19', name: 'Fantasy' },
        { id: 't3', name: 'Fiction' },
        { id: 't10', name: 'Book Club' },
        { id: 't4', name: 'Adventure' },
        { id: 't5', name: 'High Fantasy' },
        { id: 't11', name: 'Adult' },
      ],
      language: { id: 'en', name: 'English' },
    },
    {
      id: '9',
      title: 'The Sunlit Man',
      authors: [{ id: '3', fullName: 'Brandon', surname: 'Sanderson' }],
      url: 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1696146860i/60531420.jpg',
      releaseDate: '2023',
      pages: 447,
      publisher: { id: '3', name: 'Dragonsteel Entertainment' },
      synopsis:
        "Running. Putting distance between himself and the relentless Night Brigade has become a way of life. He's never been able to figure out why they want him, what he's done. Years of traceless travel have brought him to a backwater planet, where a chance meeting with a cheerful local takes him completely by surprise.",
      tags: [
        { id: 't19', name: 'Fantasy' },
        { id: 't3', name: 'Fiction' },
        { id: 't10', name: 'Book Club' },
        { id: 't4', name: 'Adventure' },
        { id: 't5', name: 'High Fantasy' },
        { id: 't11', name: 'Adult' },
      ],
      language: { id: 'en', name: 'English' },
    },
    {
      id: '12',
      title: 'Children of Dune',
      authors: [{ id: '3', fullName: 'Frank', surname: 'Herbert' }],
      url: 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1564783201i/44492286.jpg',
      releaseDate: '1976',
      pages: 609,
      publisher: { id: '3', name: 'Ace' },
      synopsis:
        "The Children of Dune are twin siblings Leto and Ghanima Atreides, whose father, the Emperor Paul Muad'Dib, disappeared in the desert wastelands of Arrakis nine years ago. Like their father, the twins possess supernormal abilities—making them valuable to their manipulative aunt Alia, who rules the Empire in the name of House Atreides.",
      tags: [
        { id: 't13', name: 'Science Fiction' },
        { id: 't14', name: 'Fiction' },
        { id: 't15', name: 'Fantasy' },
        { id: 't16', name: 'Classics' },
        { id: 't17', name: 'Adventure' },
        { id: 't18', name: 'Novel' },
      ],
      language: { id: 'en', name: 'English' },
    },
  ],
  DNF: [
    {
      id: '6',
      title: 'And Every Morning the Way Home Gets Longer and Longer',
      authors: [{ id: '2', fullName: 'Fredrik', surname: 'Backman' }],
      url: 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1472074835i/31373633.jpg',
      releaseDate: '2015',
      pages: 97,
      publisher: { id: '2', name: 'Atria Books' },
      synopsis:
        'From the New York Times bestselling author of A Man Called Ove comes a short, poignant, and bittersweet novel about a grandfather coming to terms with his dementia and the effect on his family, especially his grandson, whom he treasures.',
      tags: [
        { id: 't7', name: 'Fiction' },
        { id: 't8', name: 'Contemporary' },
        { id: 't9', name: 'Literary Fiction' },
        { id: 't10', name: 'Book Club' },
        { id: 't11', name: 'Adult' },
        { id: 't12', name: 'Friendship' },
      ],
      language: { id: 'en', name: 'English' },
    },
  ],
};
