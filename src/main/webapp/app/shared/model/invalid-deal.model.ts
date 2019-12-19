import {Moment} from 'moment';

export interface IInvalidDeal {
    id?: number;
    tagId?: number;
    fromIsoCode?: string;
    toIsoCode?: string;
    time?: Moment;
    amount?: number;
    source?: string;
    sourceFormat?: string;
    reason?: string;
}

export class InvalidDeal implements IInvalidDeal {
    constructor(
        public id?: number,
        public tagId?: number,
        public fromIsoCode?: string,
        public toIsoCode?: string,
        public time?: Moment,
        public amount?: number,
        public source?: string,
        public sourceFormat?: string,
        public reason?: string
    ) {
    }
}
