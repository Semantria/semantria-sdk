using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Semantria.Com
{
    public enum TaskStatus
    {
        UNDEFINED,
        FAILED,
        QUEUED,
        PROCESSED
    }

    public enum QueryMethod 
    { 
        GET,
        POST, 
        DELETE 
    }
}
