const API_URL: string = "";
const AUTHENTICATION_API_URL: string = API_URL + "/user"

export const environment = {
  production: true,
  authentication: {
    login: {
      signing: AUTHENTICATION_API_URL + "/public/signing",
      signup: AUTHENTICATION_API_URL + "/public/signup"
    }
  }
};
