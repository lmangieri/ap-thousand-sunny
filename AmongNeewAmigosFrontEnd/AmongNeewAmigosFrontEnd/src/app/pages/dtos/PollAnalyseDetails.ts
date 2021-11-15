export class PollAnalyseDetails {
    constructor() {
    }

    resultsWithoutIpFilter: Map<string,number>;
    resultsWithIpFilter: Map<string,number>;
    voteTitle: string;
    userVotes: Array<string>;
    totalVotesWithoutIpFilter: number;
    totalVotesWithIpFilter: number;
}