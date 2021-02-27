import { FastifyInstance, FastifyReply, FastifyRequest } from "fastify";
import { scaleIntervalByUnit } from "../utils";

async function ingressController(fastify: FastifyInstance) {
  fastify.get("/ping", async (__, reply: FastifyReply) => {
    reply.send({ message: "pong", at: new Date() });
  });

  // ---------------------------

  fastify.get("/entity", async (__, reply: FastifyReply) => {
    const params = {
      TableName: "Calls",
    };

    const hack = fastify as any;
    const items = await hack.dynamo.scan(params).promise();
    reply.send(items);
  });

  // ---------------------------

  fastify.get(
    "/entity/cps",
    { schema: { querystring: bodyJsonSchema } },
    async (request: FastifyRequest, reply: FastifyReply) => {
      const query = request.query as any;
      fastify.log.info(`GOT with query ${JSON.stringify(query)}`);
      const interval = scaleIntervalByUnit(query.interval, query.unit);
      const timeThen = Date.now() - interval;

      const params = {
        TableName: "Calls",
        KeyConditionExpression: "#acc = :accountId and #at > :then",
        ExpressionAttributeNames: {
          "#acc": "id",
          "#at": "at",
        },
        ExpressionAttributeValues: {
          ":accountId": query.accountId,
          ":then": timeThen,
        },
      };

      const hack = fastify as any;
      const items = await hack.dynamo.query(params).promise();
      reply.send({
        accountId: query.accountId,
        calls: items.Items.length,
        cps: (items.Items.length / interval) * 1000,
      });
    }
  );

  // ---------------------------

  fastify.post(
    "/entity",
    { schema: { body: bodyJsonSchema } },
    async (request: FastifyRequest, reply: FastifyReply) => {
      const body = request.body as any;
      fastify.log.info(`POSTED with body ${JSON.stringify(body)}`);

      const params = {
        TableName: "Calls",
        Item: {
          id: body.accountId,
          at: Date.now(),
          to: body.to,
          from: body.from,
          accountId: body.accountId,
          system: body.system,
        },
      };

      const hack = fastify as any;
      const put = await hack.dynamo.put(params).promise();
      reply.code(201).send(put);
    }
  );
}

export { ingressController };

/**
 * QUERY SCHEMA
 */
const querySchema = {
  accountId: { type: "string" },
  interval: { type: "number" },
  unit: {
    type: "string",
    enum: ["min", "sec", "hour"],
  },
};

/**
 * BODY SCHEMA
 */
const bodyJsonSchema = {
  type: "object",
  properties: {
    accountId: { type: "string" },
    to: { type: "string" },
    from: { type: "string" },
    system: {
      type: "string",
      enum: ["CENTRAL", "STUDIO"],
    },
  },
};
