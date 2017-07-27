import { BaseEntity } from './../../shared';

const enum RetweetAccountStatus {
    'IN_PROGRESS',
    'STOPPED'
}

export class RetweetAccount implements BaseEntity {
    constructor(
        public id?: number,
        public status?: RetweetAccountStatus,
        public userid?: string,
        public username?: string,
        public keywords?: string,
        public stop?: boolean,
        public created?: any,
    ) {
        this.stop = false;
    }
}
