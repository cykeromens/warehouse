export interface IFileLoader {
    id?: number;
    file?: any;
    fileContentType?: any;
}

export class FileLoader implements IFileLoader {
    constructor(public id?: number, public file?: any, public fileContentType?: any) {
    }
}
