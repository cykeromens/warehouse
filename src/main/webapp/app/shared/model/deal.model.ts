import { Moment } from 'moment';

export interface IDeal {
    id?: number;
    tagId?: number;
    fromIsoCode?: string;
    toIsoCode?: string;
    time?: Moment;
    amount?: number;
    source?: string;
    sourceFormat?: string;
}

export class Deal implements IDeal {
    constructor(
        public id?: number,
        public tagId?: number,
        public fromIsoCode?: string,
        public toIsoCode?: string,
        public time?: Moment,
        public amount?: number,
        public source?: string,
        public sourceFormat?: string
    ) {}
}
