export class HomeDetailsDto {
    constructor() {
    }
    pollControlStatus: number;
    voteTitle: string;
    voteOptions: Map<number, string>;
    hasUserVoted: boolean;
    userVote: string;

    titleLastPoll: string;
    resultsLastPoll: Map<string,number>;
    totalVotes: number;

}