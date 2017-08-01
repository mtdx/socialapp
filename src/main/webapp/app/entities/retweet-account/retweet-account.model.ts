import {BaseEntity} from './../../shared';

const enum RetweetAccountStatus {
    'IN_PROGRESS',
    'LOCK',
    'STOPPED'
}

export class RetweetAccount implements BaseEntity {
    constructor(
        public id?: number,
        public status?: RetweetAccountStatus,
        public username?: string,
        public tweetId?: string,
        public stop?: boolean,
    ) {
        this.stop = false;
    }
}
