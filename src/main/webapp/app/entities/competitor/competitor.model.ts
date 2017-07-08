import { BaseEntity } from './../../shared';

export const enum CompetitorStatus {
    'IN_PROGRESS',
    'DONE',
    'LOCK',
    'STOPPED'
}

export class Competitor implements BaseEntity {
    constructor(
        public id?: number,
        public status?: CompetitorStatus,
        public userid?: string,
        public username?: string,
        public likes?: number,
        public cursor?: number,
        public stop?: boolean,
        public reset?: boolean,
    ) {
        this.stop = false;
        this.reset = false;
    }
}
