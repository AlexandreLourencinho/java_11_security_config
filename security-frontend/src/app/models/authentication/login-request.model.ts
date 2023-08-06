export default class LoginRequestModel {

  constructor(username?: string, password?: string) {
    this.username = username;
    this.password = password;
  }

  private username: string | undefined;
  private password: string | undefined;

  public getUsername(): string | undefined {
    return this.username;
  }

  public setUsername(username: string): this {
    this.username = username;
    return this;
  }

  public setPassword(password: string): this {
    this.password = password;
    return this;
  }
}
