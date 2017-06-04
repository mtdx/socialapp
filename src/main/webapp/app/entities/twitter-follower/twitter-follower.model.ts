import { Competitor } from '../competitor';
export class TwitterFollower {
    constructor(
        public id?: number,
        public userid?: string,
        public accountAge?: number,
        public likes?: number,
        public followers?: number,
        public tweets?: number,
        public username?: string,
        public like?: boolean,
        public follow?: boolean,
        public updated?: any,
        public competitor?: Competitor,
    ) {
        this.like = false;
        this.follow = false;
    }
}
