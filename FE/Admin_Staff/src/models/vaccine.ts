export interface Vaccine {
  id?: number;
  vaccineName: string;
  price: number;
  description?: string;
  vaccineAge?: string;
  dateOfManufacture: string;
  vaccineExpiryDate?: string;
}

export interface VaccinationHistory {
  id: number
  patientId: number
  vaccineName: string
  date: string
}

export interface Feedback {
  id: number
  patientId: number
  date: string
  comment: string
  rating: number
}