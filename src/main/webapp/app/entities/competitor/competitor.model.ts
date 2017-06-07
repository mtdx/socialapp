
const enum CompetitorStatus {
    'IN_PROGRESS',
    'DONE',
    'LOCK'

};
export class Competitor {
    constructor(
        public id?: number,
        public status?: CompetitorStatus,
        public userid?: string,
        public username?: string,
        public likes?: number,
        public cursor?: number,
    ) {
    }
}
