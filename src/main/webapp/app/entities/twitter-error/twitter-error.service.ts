import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { TwitterError } from './twitter-error.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TwitterErrorService {

    private resourceUrl = 'api/twitter-errors';
    private resourceSearchUrl = 'api/_search/twitter-errors';

    constructor(private http: Http) { }

    find(id: number): Observable<TwitterError> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(twitterError: TwitterError): TwitterError {
        const copy: TwitterError = Object.assign({}, twitterError);
        return copy;
    }
}
