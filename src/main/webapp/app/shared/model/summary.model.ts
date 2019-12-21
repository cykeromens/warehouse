import {Moment} from 'moment';

export interface ISummary {
    id?: string;
    fileName?: string;
    duration?: number;
    total?: number;
    valid?: number;
    invalid?: number;
    date?: Moment;
}

export class Summary implements ISummary {
    constructor(
        public id?: string,
        public fileName?: string,
        public duration?: number,
        public total?: number,
        public valid?: number,
        public invalid?: number,
        public date?: Moment
    ) {
    }
}
