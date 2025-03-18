import { Patient, UserRole } from "../../models/user";

export const sampleParent: Patient = {
  id: 1,
  username: "johndoe",
  fullName: "John Doe",
  dateOfBirth: "1980-05-15",
  phone: "123-456-7890",
  email: undefined,
  role: UserRole.CUSTOMER,
  avatar: "/placeholder.svg",
  vaccineStatus: "Fully Vaccinated",
  children: [
    {
      id: 2,
      username: "childdoe",
      fullName: "Child Doe",
      dateOfBirth: "2015-03-10",
      phone: "",
      role: UserRole.CHILD,
      avatar: "/placeholder.svg",
      vaccineStatus: "Partially Vaccinated",
      email: undefined
    },
  ],
};

export const sampleChild: Patient = {
  id: 2,
  username: "childdoe",
  fullName: "Child Doe",
  dateOfBirth: "2015-03-10",
  phone: "",
  role: UserRole.CHILD,
  avatar: "/placeholder.svg",
  vaccineStatus: "Partially Vaccinated",
  email: undefined,
  parent: {
    id: 1,
    username: "johndoe",
    fullName: "John Doe",
    dateOfBirth: "1980-05-15",
    phone: "123-456-7890",
    role: UserRole.CUSTOMER,
    avatar: "/placeholder.svg",
    vaccineStatus: "Fully Vaccinated",
    email: undefined
  },
};

export const sampleParentNoChildren: Patient = {
  id: 3,
  username: "maryjane",
  fullName: "Mary Jane",
  dateOfBirth: "1985-10-10",
  phone: "111-222-3333",
  role: UserRole.CUSTOMER,
  avatar: "/placeholder.svg",
  vaccineStatus: "Fully Vaccinated",
  children: [],
  email: undefined
};
