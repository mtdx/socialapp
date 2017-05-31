import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Competitor } from './competitor.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CompetitorService {

    private resourceUrl = 'api/competitors';
    private resourceSearchUrl = 'api/_search/competitors';

    constructor(private http: Http) { }

    create(competitor: Competitor): Observable<Competitor> {
        const copy = this.convert(competitor);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(competitor: Competitor): Observable<Competitor> {
        const copy = this.convert(competitor);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Competitor> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
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

    private convert(competitor: Competitor): Competitor {
        const copy: Competitor = Object.assign({}, competitor);
        return copy;
    }
}
