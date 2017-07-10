import { BaseEntity } from './../../shared';

const enum KeywordStatus {
    'IN_PROGRESS',
    'DONE',
    'LOCK',
    'STOPPED'
}

export class TwitterKeyword implements BaseEntity {
    constructor(
        public id?: number,
        public status?: KeywordStatus,
        public keyword?: string,
        public competitors?: number,
        public page?: number,
        public stop?: boolean,
        public reset?: boolean,
        public created?: any,
    ) {
        this.stop = false;
        this.reset = false;
    }
}
