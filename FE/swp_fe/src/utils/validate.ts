export class Validate {
  static email(mail: string) {
    return /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(mail);
  }

  static formatPrice(price: number): string {
    return new Intl.NumberFormat("vi-VN").format(price);
  }
}

export function parseJWT(token: string): string | null {
  try {
    const base64Url = token.split('.')[1];
    if (!base64Url) {
      throw new Error("Invalid token");
    }
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        })
        .join('')
    );
    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error("Error parsing JWT:", error);
    return null;
  }
}
