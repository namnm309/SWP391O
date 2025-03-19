export interface Children {
  userId: number;
  fullname: string;
  avatarUrl: string;
  birthDate: string | null;
  gender: string | null;
  height: string | null;
  weight: string | null;
  relatives: Rela[];
}
export interface HistoryChildren {
  orderDetailId: number;
  vaccineName: string;
  vaccinationDate: string;
  quantity: number;
}
export interface UpcomingChildren {
  orderDetailId: number;
  vaccineName: string;
  vaccinationDate: string;
  remainingDoses: number;
}


export interface Rela {
  relativeId: number;
  fullname: string;
  relationshipType: string;
}
