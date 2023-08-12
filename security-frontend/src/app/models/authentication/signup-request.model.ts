/**
 * the model used for signup requests
 */
export default class SignupRequestModel {

  private username: string | undefined;
  private password: string | undefined;
  private mail: string | undefined;

  constructor(username?: string | undefined, password?: string | undefined, mail?: string | undefined) {
    this.username = username;
    this.password = password;
    this.mail = mail;
  }

  public setUsername(username: string): SignupRequestModel {
    this.username = username;
    return this;
  }

  public setPassword(password: string): SignupRequestModel {
    this.password = password;
    return this;
  }

  public setMail(mail: string): SignupRequestModel {
    this.mail = mail;
    return this;
  }

  public getUsername(): string | undefined {
    return this.username;
  }

  public getMail(): string | undefined {
    return this.mail
  }
}
