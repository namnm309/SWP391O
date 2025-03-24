export enum RoleUser {
  ADMIN = "ADMIN",
  STAFF = "STAFF",
  CUSTOMER = "ROLE_CUSTOMER",
  CHILD = "ROLE_CHILD",
}

export enum GenderUser {
  MALE = "MALE",
  FEMALE = "FEMALE",
}

export enum AppointmentStatus {
  PENDING = "PENDING",
  CONFIRMED = "CONFIRMED",
  COMPLETED = "COMPLETED",
  CANCELLED = "CANCELLED",
}

export enum PaymentStatus {
  PENDING = "PENDING",
  PAID = "PAID",
  REFUNDED = "REFUNDED",
  FAILED = "FAILED",
}

export enum FeedbackStatus {
  PUBLISHED = "PUBLISHED",
  HIDDEN = "HIDDEN",
  FLAGGED = "FLAGGED",
}

export enum RelationshipType {
  BROTHER_SISTER = "ANH_CHI",
  UNCLE_AUNT = "CHU_THIEM",
  PARENTS = "CHA_ME",
  GRANDMASTERS = "ONG_BA"
}