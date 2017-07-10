import { BaseEntity } from './../../shared';

export class TwitterSettings implements BaseEntity {
    constructor(
        public id?: number,
        public maxLikes?: number,
        public hasDefaultProfileImage?: boolean,
        public hasNoDescription?: boolean,
        public accountAgeLessThan?: number,
        public minActivity?: number,
        public followingToFollowersRatio?: number,
        public likesToTweetsRatio?: number,
        public notLikeTweetsOlderThan?: number,
        public retweetPercent?: number,
        public minCompetitorFollowers?: number,
    ) {
        this.hasDefaultProfileImage = false;
        this.hasNoDescription = false;
    }
}
