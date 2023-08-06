// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

const API_URL: string = "http://localhost:8080";
const AUTHENTICATION_API_URL: string = API_URL + "/user"

export const environment = {
  production: false,
  authentication: {
    login: {
      signing: AUTHENTICATION_API_URL + "/public/signing",
      signup: AUTHENTICATION_API_URL + "/public/signup"
    }
  }
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
