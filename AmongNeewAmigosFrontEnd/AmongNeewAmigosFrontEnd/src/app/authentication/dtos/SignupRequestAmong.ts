export class SignupRequestAmong {
    constructor(email : string, password : string, nickName : string) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }

    email  : string;
    password  : string;
    nickName  : string;
}