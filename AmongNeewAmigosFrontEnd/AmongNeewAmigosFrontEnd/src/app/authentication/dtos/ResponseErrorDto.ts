export class ResponseErrorDto {
    code: string;
    message: string;

    assign(responseErrorDto : ResponseErrorDto) {
        this.code = responseErrorDto.code;
        this.message = responseErrorDto.message;
    }

    clean() {
        this.code = '';
        this.message = '';
    }
}