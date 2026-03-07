export type BookClubDetails = {
  id: string;
  details: string;
  imageBase64?: string;
};
export interface BookClubMemberDTO {
  id: string;
  name: string;
  picture: string;
}

export type JoinBookClubDTO = {
  bookClubName: string;
  members: BookClubMemberDTO[];
  imageBase64: string | null;
};
