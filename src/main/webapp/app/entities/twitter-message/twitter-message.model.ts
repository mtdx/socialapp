import { BaseEntity } from './../../shared';

export class TwitterMessage implements BaseEntity {
    constructor(
        public id?: number,
        public accountName?: string,
        public accountDescription?: string,
        public accountUrl?: string,
        public accountLocation?: string,
    ) {
    }
}
