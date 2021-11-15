export class PollDto {
    constructor(title : string, options: string[]) {
        this.title = title;
        this.options = options;
    }
    title: string;
    options: string[];
}