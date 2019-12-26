import {Moment} from 'moment';

export interface ISummary {
    id?: string;
    source?: string;
    duration?: number;
    total?: number;
    valid?: number;
    invalid?: number;
    date?: Moment;
}

export class Summary implements ISummary {
    constructor(
        public id?: string,
        public source?: string,
        public duration?: number,
        public total?: number,
        public valid?: number,
        public invalid?: number,
        public date?: Moment
    ) {
    }
}
