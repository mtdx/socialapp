import { BaseEntity } from './../../shared';

export class Proxy implements BaseEntity {
    constructor(
        public id?: number,
        public host?: string,
        public port?: number,
        public username?: string,
        public password?: string,
    ) {
    }
}
