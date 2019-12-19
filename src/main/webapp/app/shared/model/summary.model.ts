export interface ISummary {
    id?: number;
    fileName?: string;
    processDuration?: number;
    totalImported?: number;
    totalValid?: number;
    totalNotValid?: number;
}

export class Summary implements ISummary {
    constructor(
        public id?: number,
        public fileName?: string,
        public processDuration?: number,
        public totalImported?: number,
        public totalValid?: number,
        public totalNotValid?: number
    ) {
    }
}
